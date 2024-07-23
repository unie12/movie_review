function updateActivityList(response) {
    var container = $('#activity-list');
    container.empty();

    var items = response.favoriteMovies;

    if (items && items.length > 0) {
        $('#activity-container').show();
        $('#empty-message').hide();

        items.forEach(function(item) {
            var element = createActivityElement(item);
            container.append(element);
        });

        setFavoriteButtonListeners();
    } else {
        $('#activity-container').hide();
        $('#empty-message').show();
    }
}

function createActivityElement(item) {
    console.log('favorite item: ', item);

    var element = $('<div>').addClass('activity-item');
    var ajouRatingAvg = item.movieCommonDTO.ajou_rating;
    var ajouRatingCnt = item.movieCommonDTO.ajou_rating_cnt;
    var ajouRatingText = ajouRatingAvg + ' (' + ajouRatingCnt + '표)';

    element.html(`
        <img src="https://image.tmdb.org/t/p/w500${item.movieCommonDTO.poster_path}"
             alt="${item.movieCommonDTO.title}"
             onclick="navigateToMovieDetails(${item.movieCommonDTO.tid})">
        <p class="activity-title">${item.movieCommonDTO.title}</p>
        <p><strong>아주대 평점:</strong> <span>${ajouRatingText}</span></p>

        <button id="favoriteButton-${item.movieCommonDTO.id}"
                data-activity-id="${item.movieCommonDTO.id}"
                class="favorite active">
            <i class="fas fa-heart"></i>
        </button>
    `);

    return element;
}

function setFavoriteButtonListeners() {
    $('.favorite').click(function() {
        var $button = $(this);
        var movieId = $(this).data('activity-id');
        var isFavorite = $(this).hasClass('active');
        console.log('movieId, isFavorite', movieId, isFavorite);

        $.ajax({
            url: '/api/movie/favorite',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ movieId: movieId, favorite: !isFavorite }),
            success: function(response) {
                if (response.success) {
                    if (!response.isFavorite) {
                        $button.closest('.activity-item').fadeOut(function() {
                            $(this).remove();
                            if ($('.activity-item').length === 0) {
                                $('#activity-container').hide();
                                $('#empty-message').show();
                            }
                        });
                    }
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
    });
}
