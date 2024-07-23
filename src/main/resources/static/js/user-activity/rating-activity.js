function updateActivityList(response) {
    var container = $('#activity-list');
    container.empty();

    var items = response.ratings;

    if (items && items.length > 0) {
        $('#activity-container').show();
        $('#empty-message').hide();

        items.forEach(function(item) {
            var element = createActivityElement(response.category, item);
            container.append(element);
        });

    } else {
        $('#activity-container').hide();
        $('#empty-message').show();
    }
}

function createActivityElement(category, item) {
    var ajouRatingAvg = item.movie.ajou_rating;
    var ajouRatingCnt = item.movie.ajou_rating_cnt;
    var ajouRatingText = ajouRatingAvg + ' (' + ajouRatingCnt + '표)';

    var element = $('<div>').addClass('activity-item');
    switch(category) {
        case 'rating':
            console.log('rating item: ', item);

            var scoreText = item.score !== null ? item.score : '미정';

            element.html(`
                <img src="https://image.tmdb.org/t/p/w500${item.movie.poster_path}"
                     alt="${item.title}"
                     onclick="navigateToMovieDetails(${item.movie.tid})">
                <p class="activity-title">${item.movie.title}</p>
                <span class="user-rating">평점: <span class="rating-value">${scoreText}</span></span>
                <p><strong>아주대 평점:</strong> <span>${ajouRatingText}</span></p>

            `);

            break;
    }
    return element;
}
