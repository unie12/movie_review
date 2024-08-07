window.activityHandlers = window.activityHandlers || {};
window.activityHandlers.heart = {
    updateActivityList: function(response, append) {
        var container = $('#activity-list');
        if(!append) {
            container.empty();
        }

        var reviews = response.reviews;

        if (reviews && reviews.length > 0) {
            $('#activity-container').show();
            $('#empty-message').hide();

            reviews.forEach((review) => {
                var element = this.createReviewElement(review);
                container.append(element);
            });

            // 좋아요 버튼 이벤트 리스너 설정
            setupLikeButtons();
        } else if (!append){
            $('#activity-container').hide();
            $('#empty-message').show();
        }
    },

    createReviewElement: function(review) {
        var element = $('<div>').addClass('review-card');
        var scoreText = review.reviewDTO.userRating !== null ? review.reviewDTO.userRating : '미정';
        var likedClass = 'active';

        var ajouRatingAvg = review.movieCommonDTO.ajou_rating;
        var ajouRatingCnt = review.movieCommonDTO.ajou_rating_cnt;
        var ajouRatingText = ajouRatingAvg + ' (' + ajouRatingCnt + '표)';
        var reviewTextWithBreaks = review.reviewDTO.review.text.replace(/\n/g, '<br>');

        element.html(`
            <div class="poster-container">
                <img src="https://image.tmdb.org/t/p/w500${review.movieCommonDTO.poster_path}"
                     alt="${review.movieCommonDTO.title}"
                     onclick="navigateToMovieDetails(${review.movieCommonDTO.tid})">
                <p class="movie-title">${review.movieCommonDTO.title}</p>
                <p><strong>아주대 평점:</strong> <span>${ajouRatingText}</span></p>

            </div>
            <div class="review-content">
                <div class="user-info">
                    <img src="${review.reviewDTO.user.picture}"
                        alt="User Picture"
                        onclick="navigateToUser('${review.reviewDTO.user.email}')"
                        class="user-picture">
                    <span class="user-nickname">${review.reviewDTO.user.nickname}</span>
                    <span class="user-rating">평점: <span class="rating-value">${scoreText}</span></span>
                    <span class="user-heart like-button ${likedClass}" data-review-id="${review.reviewDTO.review.id}">
                        좋아요: <span class="heart-value like-count">${review.reviewDTO.heartCnt}</span>
                    </span>
                </div>
                <p class="review-text">${reviewTextWithBreaks}</p>
            </div>
        `);

        return element;
    }
}
function setupLikeButtons() {
    $('.like-button').click(function() {
        var reviewId = $(this).data('review-id');
        var $button = $(this);
        var isLiked = $button.hasClass('active');

        $.ajax({
            url: '/api/review/heart/' + reviewId,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({reviewId: reviewId, heart: !isLiked}),
            success: function(response) {
                $button.toggleClass('active', response.heart);
                $button.find('.like-count').text(response.updateHeartCnt);
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
    });
}