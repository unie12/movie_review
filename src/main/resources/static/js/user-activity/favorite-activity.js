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
                    <button class="favorite-button ${item.favorite ? 'active' : ''}" data-movie-id="${item.movieCommonDTO.id}">
                        <i class="fas fa-heart"></i>
                    </button>
                </div>
            </div>
            <div class="fav-movie-info">
                <p class="fav-movie-title">${item.movieCommonDTO.title}</p>
            </div>
        `);

        return element;
    },

    setFavoriteButtonListeners: function() {
        $('.favorite-button').off('click').on('click', function(e) {
            e.stopPropagation();
            var $button = $(this);
            var movieId = $button.data('movie-id');
            var isFavorite = $button.hasClass('active');

            window.activityHandlers.favorite.toggleFavorite($button, movieId, !isFavorite);
        });
    },

    toggleFavorite: function($button, movieId, setFavorite) {
        $.ajax({
            url: '/api/movie/favorite',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ movieId: movieId, favorite: setFavorite }),
            success: function(response) {
                if (response.success) {
                    if (setFavorite) {
                        $button.addClass('active');
                    } else {
                        $button.removeClass('active');
                    }
                } else {
                    console.error('Error: ', response.message);
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


