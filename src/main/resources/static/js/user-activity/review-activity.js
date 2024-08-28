window.activityHandlers = window.activityHandlers || {};
window.activityHandlers.review = {
    updateActivityList: function(response, append) {
        var container = $('#activity-list');
        if (!append) {
            container.empty();
        }
        var reviews = response.reviews;

        if (reviews && reviews.length > 0) {
            $('#activity-container').show();
            $('#empty-message').hide();

            reviews.forEach((review) => {
                var element = this.createReviewElement(review);
                container.append(element);
                attachLikeEvents(element);
                attachLikeCountEvents(element);
            });


        } else if(!append){
            $('#activity-container').hide();
            $('#empty-message').show();
        }
    },

    createReviewElement: function(review) {
        var element = $('<div>').addClass('review-card');
        var scoreText = review.reviewDTO.userRating !== null ? review.reviewDTO.userRating : '미정';
        var likedClass = review.reviewDTO.likedByCurrentUser ? 'active' : '';
        var starRating = createStarRating(scoreText);

        var ajouRatingAvg = review.movieCommonDTO.ajou_rating;
        var ajouRatingCnt = review.movieCommonDTO.ajou_rating_cnt;
        var ajouRatingText = ajouRatingAvg + ' (' + ajouRatingCnt + '표)';
        var reviewTextWithBreaks = review.reviewDTO.review.text.replace(/\n/g, '<br>');
        var isActive = review.reviewDTO.likedByCurrentUser ? 'active' : '';
        var userRoleClass = review.reviewDTO.user.role;

        var userNicknameStyle = review.reviewDTO.user.role && review.reviewDTO.user.role.color
                ? `style="color: ${review.reviewDTO.user.role.color}"` : '';

        element.html(`
            <div class="review-header">
                <div class="poster-container">
                    <img src="https://image.tmdb.org/t/p/w500${review.movieCommonDTO.poster_path}"
                         alt="${review.movieCommonDTO.title}"
                         onclick="navigateToMovieDetails(${review.movieCommonDTO.tid})">
                    <p class="movie-title">${review.movieCommonDTO.title}</p>
                </div>
                <div class="review-content">
                    <div class="user-info">
                        <div class="user-details">
                            <img src="${review.reviewDTO.user.picture}" alt="User Picture" class="user-picture"
                            onclick="navigateToUser('${review.reviewDTO.user.email}')">
                            <span class="user-nickname ${userRoleClass}">${review.reviewDTO.user.nickname}</span>
                            <span class="upload-date" data-upload-date=${review.reviewDate}></span>
                        </div>
                        <div class="user-rating-container">
                            ${starRating}
                        </div>
                    </div>
                    <p class="review-text">${reviewTextWithBreaks}</p>
                </div>
            </div>
            <div class="review-footer">
                <p><strong>아주대 평점:</strong> <span>${ajouRatingText}</span></p>
                <div class="like-container">
                    <button class="btn btn-outline-primary like-button ${isActive}" data-review-id="${review.reviewDTO.review.id}">
                        <i class="fas fa-heart"></i>
                    </button>
                    <a href="#" class="like-count" data-review-id="${review.reviewDTO.review.id}">좋아요</a> <span class="heart-count">${review.reviewDTO.heartCnt}</span>
                </div>
            </div>
        `);

        var uploadDateElement = element.find('.upload-date')[0];
        uploadDateElement.textContent = formatRelativeTime(review.reviewDate);
        return element;
    }
}

function setupLikeButtons() {
//    $('.like-button').click(function() {
    $('#activity-list').off('click', '.like-button').on('click', '.like-button', function() {
        console.log('Like button clicked', new Date().toISOString());

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

function attachLikeEvents(element) {
    $(element).find('.like-button').on('click', function() {
        var reviewId = $(this).data('review-id');
        var $button = $(this);

        $.ajax({
            url: '/api/review/heart/' + reviewId,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({reviewId: reviewId, heart: !$button.hasClass('active')}),
            success: function(response) {

                if (response.heart) {
                    $button.addClass('active');
                } else {
                    $button.removeClass('active');
                }
                $button.closest('.review-card').find('.heart-count').text(response.updateHeartCnt);
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
    });
}

function attachLikeCountEvents(element) {
    $(element).find('.like-count').on('click', function(e) {
        e.preventDefault();
        var reviewId = $(this).data('review-id');
        openLikeModal(reviewId);
    });
}

function openLikeModal(reviewId) {
    let page = 0;
    const size = 10;
    const userList = document.getElementById('likeUserList');
    userList.innerHTML = '';

    loadLikes();

    function loadLikes() {
        fetch(`/api/review/${reviewId}/likes?page=${page}&size=${size}`)
            .then(response => response.json())
            .then(data => {
                data.content.forEach(user => {
                    userList.innerHTML += `
                        <li>
                            <a href="/info/${user.email}">
                                <img src="${user.picture}" alt="${user.nickname}" class="user-picture">
                            </a>
                            <span>${user.nickname}</span>
                        </li>
                    `;
                });
                page++;
                if (data.last) {
                    userList.removeEventListener('scroll', handleScroll);
                }
            });
    }
    function handleScroll() {
        if (userList.scrollTop + userList.clientHeight >= userList.scrollHeight - 5) {
            loadLikes();
        }
    }

    userList.addEventListener('scroll', handleScroll);
    document.getElementById('likeModal').style.display = 'block';
}

function formatRelativeTime(dateString) {
    const now = new Date();
    const uploadDate = new Date(dateString);
    const diffInMilliseconds = now - uploadDate;
    const diffInMinutes = Math.floor(diffInMilliseconds / (1000 * 60));
    const diffInHours = Math.floor(diffInMinutes / 60);
    const diffInDays = Math.floor(diffInHours / 24);

    if (diffInMinutes < 60) {
        return `${diffInMinutes}분 전`;
    } else if (diffInHours < 24) {
        return `${diffInHours}시간 전`;
    } else {
        return `${diffInDays}일 전`;
    }
}

function updateRelativeTimes() {
    var uploadDates = document.querySelectorAll('.upload-date');

    uploadDates.forEach(element => {
        const dateString = element.getAttribute('data-upload-date');
        element.textContent = formatRelativeTime(dateString);
    });
}

document.addEventListener('DOMContentLoaded', updateRelativeTimes);
setInterval(updateRelativeTimes, 60000);

