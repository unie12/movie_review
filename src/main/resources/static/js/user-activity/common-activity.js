let currentPage = 0;
let isLoading = false;
let hasMoreData = true;

function loadActivities(append = true) {
    if (isLoading || !hasMoreData) return;
    isLoading = true;

    var sortOption = $("#sorting").val() || defaultSort;

    $.ajax({
        url: `/api/user/${userEmail}/activities`,
        type: 'GET',
        data: {
            category: category,
            sort: sortOption,
            page: currentPage,
            size: 9
        },
        success: function(response) {
            let activityItems = response.favoriteMovies || response.reviews || response.ratings || response.subscriptionDTOs || [];

            if(activityItems.length > 0) {
                if (window.activityHandlers && window.activityHandlers[category]) {
                    window.activityHandlers[category].updateActivityList(response, append);
                } else {
                    console.error('Handler not found for category:', category);
                }
                currentPage++;
                if(activityItems.length < 9) {
                    hasMoreData = false;
                }
            } else {
                hasMoreData = false;
            }

            if (!hasMoreData) {
                $(window).off('scroll', scrollHandler);
            }

            isLoading = false;

            if (currentPage === 1 && activityItems.length === 0) {
                $('#empty-message').show();
                $('#activity-container').hide();
            }
            updateScrollListener();
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
            isLoading = false;
        }
    });
}

function scrollHandler() {
    if($(window).scrollTop() + $(window).height() > $(document).height() - 50) {
        loadActivities(true);
    }
}

function updateScrollListener() {
    $(window).off('scroll', scrollHandler);
    if (hasMoreData) {
        $(window).on('scroll', scrollHandler);
    }
}

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

function navigateToMovieDetails(movieId) {
    window.location.href = '/contents/' + movieId;
}

function navigateToUser(email) {
    window.location.href = '/info/' + email;
}

$(document).ready(function() {
    initializedActivityList();
    loadSortOptions();
    loadActivities();

    $('#sorting').change(function() {
        currentPage = 0;
        hasMoreData = true;
        isLoading = false;
        $('#activity-list').empty()
        loadActivities(false);
    });

    updateScrollListener();
});

$(document).on('click', '.close', function() {
    $('#likeModal').hide();
});

$(document).on('click', function(event) {
    if ($(event.target).is('#likeModal')) {
        $('#likeModal').hide();
    }
});