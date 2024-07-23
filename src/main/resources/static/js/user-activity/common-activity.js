$(document).ready(function() {
    initializedActivityList();
    loadSortOptions();
    loadActivities();

    $('#sorting').change(function() {
        loadActivities();
    });
});

function initializedActivityList() {
    var $activityList = $('#activity-list');
    switch(category) {
        case 'review':
        case 'heart':
            $activityList.addClass('review-grid');
            break;
        case 'subscriber':
        case 'subscription':
            $activityList.addClass('subscription-container');
            break;
        case 'favorite':
        case 'rating':
            $activityList.addClass('fav-rat-grid');
            break;
    }
}

function loadSortOptions() {
    $.ajax({
        url: `/api/user/${userEmail}/sort-options`,
        type: 'GET',
        success: function(options) {
            var select = $('#sorting');
            select.empty();
            options[category].forEach(function(option) {
                select.append($('<option>', {
                    value: option.key,
                    text: option.description
                }));
            });
        },
        error: function(xhr, status, error) {
            console.error('Error loading sort options:', error);
        }
    });
}

function loadActivities() {
    var sortOption = $("#sorting").val() || defaultSort;

    $.ajax({
        url: `/api/user/${userEmail}/activities`,
        type: 'GET',
        data: {
            category: category,
            sort: sortOption,
            page: 0,
            size: 20
        },
        success: function(response) {
            updateActivityList(response);
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}


function navigateToMovieDetails(movieId) {
    window.location.href = '/contents/' + movieId;
}

function navigateToUser(email) {
    window.location.href = '/info/' + email;
}