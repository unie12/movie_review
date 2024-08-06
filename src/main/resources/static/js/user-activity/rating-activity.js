window.activityHandlers = window.activityHandlers || {};
window.activityHandlers.rating = {
    updateActivityList: function(response, append) {
        var container = $('#activity-list');
        if (!append) {
            container.empty();
        }

        var items = response.ratings;

        if (items && items.length > 0) {
            $('#activity-container').show();
            $('#empty-message').hide();

            items.forEach((item) => {
                var element = this.createActivityElement(item);
                container.append(element);
            });

        } else if(!append){
            $('#activity-container').hide();
            $('#empty-message').show();
        }
    },

    createActivityElement: function(item) {
        var ajouRatingAvg = item.movie.ajou_rating;
        var ajouRatingCnt = item.movie.ajou_rating_cnt;
        var ajouRatingText = ajouRatingAvg + ' (' + ajouRatingCnt + '표)';

        var element = $('<div>').addClass('activity-item');
        var scoreText = item.score !== null ? item.score : '미정';

        element.html(`
            <img src="https://image.tmdb.org/t/p/w500${item.movie.poster_path}"
                 alt="${item.title}"
                 onclick="navigateToMovieDetails(${item.movie.tid})">
            <p class="activity-title">${item.movie.title}</p>
            <span class="user-rating">평점: <span class="rating-value">${scoreText}</span></span>
            <p><strong>아주대 평점:</strong> <span>${ajouRatingText}</span></p>

        `);

        return element;
    }
}
