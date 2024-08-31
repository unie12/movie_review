window.activityHandlers = window.activityHandlers || {};
window.activityHandlers.favorite = {
    updateActivityList: function(response, append) {
        var container = $('#activity-list');
        if (!append) {
            container.empty();
        }
        var items = response.favoriteMovies;

        if (items && items.length > 0) {
            $('#activity-container').show();
            $('#empty-message').hide();

            items.forEach((item) => {
                var element = this.createActivityElement(item);
                container.append(element);
            });

            this.setFavoriteButtonListeners();
        } else if(!append){
            $('#activity-container').hide();
            $('#empty-message').show();
        }
    },

     createActivityElement: function(item) {
        var element = $('<div>').addClass('activity-item');
        var ajouRatingAvg = item.movieCommonDTO.ajou_rating;
        var ajouRatingCnt = item.movieCommonDTO.ajou_rating_cnt;
        var ajouRatingText = ajouRatingAvg + ' (' + ajouRatingCnt + '표)';

        element.html(`
            <div class="fav-poster-container" onclick="navigateToMovieDetails(${item.movieCommonDTO.tid})">
                <img src="https://image.tmdb.org/t/p/w500${item.movieCommonDTO.poster_path}"
                     alt="${item.movieCommonDTO.title}">
                <div class="fav-poster-overlay">
                    <button class="favorite-button active" data-movie-id="${item.movieCommonDTO.id}">
                        <i class="fas fa-heart"></i>
                    </button>
                </div>
            </div>
            <div class="fav-movie-info">
                <p class="fav-movie-title">${item.movieCommonDTO.title}</p>
            </div>
            <div class="unfavorite-confirm" style="display:none;">
                <p class="undo-text">좋아요 해제 중...</p>
                <button class="undo-button">실행 취소</button>
            </div>
        `);

        return element;
    },

    setFavoriteButtonListeners: function() {
            $('.favorite-button').off('click').on('click', function(e) {
                e.stopPropagation();
                var $button = $(this);
                var $activityItem = $button.closest('.activity-item');
                var movieId = $button.data('movie-id');

                if ($button.hasClass('active')) {
                    $activityItem.find('.unfavorite-confirm').slideDown();

                    var timeoutId = setTimeout(function() {
                        window.activityHandlers.favorite.removeFavorite($activityItem, movieId);
                    }, 4000);

                    $activityItem.data('timeoutId', timeoutId);

                    $activityItem.find('.undo-button').on('click', function(e) {
                        e.stopPropagation();
                        clearTimeout($activityItem.data('timeoutId'));
                        $activityItem.find('.unfavorite-confirm').slideUp();
                    });
                } else {
                    window.activityHandlers.favorite.addFavorite($button, movieId);
                }
            });
        },

        removeFavorite: function($activityItem, movieId) {
            $.ajax({
                url: '/api/movie/favorite',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ movieId: movieId, favorite: false }),
                success: function(response) {
                    if (response.success) {
                        $activityItem.fadeOut(function() {
                            $(this).remove();
                            if ($('.activity-item').length === 0) {
                                $('#activity-container').hide();
                                $('#empty-message').show();
                            }
                        });
                    } else {
                        console.error('Error:', response.message);
                    }
                },
                error: function(xhr, status, error) {
                    if(xhr.status === 401) {
                        window.location.href = '/oauth2/authorization/google';
                    } else {
                        console.error('Error:', error);
                    }
                }
            });
        },

        addFavorite: function($button, movieId) {
            $.ajax({
                url: '/api/movie/favorite',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ movieId: movieId, favorite: true }),
                success: function(response) {
                    if (response.success) {
                        $button.addClass('active');
                    } else {
                        console.error('Error:', response.message);
                    }
                },
                error: function(xhr, status, error) {
                    if(xhr.status === 401) {
                        window.location.href = '/oauth2/authorization/google';
                    } else {
                        console.error('Error:', error);
                    }
                }
            });
        }
}


