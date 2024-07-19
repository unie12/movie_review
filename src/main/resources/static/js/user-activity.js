    var userEmail = /*[[${userEmail}]]*/ '';
    var category = 'favorite'; // 현재 페이지 카테고리

    function navigateToMovieDetails(movieId) {
        console.log('navigate movie info: ', movieId);

        window.location.href = '/contents/' + movieId;
    }

    $(document).ready(function() {
        loadSortOptions();
        loadActivities();

        $('#sorting').change(function() {
            loadActivities();
        });
    });

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
        var sortOption = $("#sorting").val();
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

    function updateActivityList(response) {
        var container = $('.activity-grid');
        container.empty();

        var items;
        switch(response.category) {
            case 'favorite':
                items = response.favoriteMovies;
                break;
<!--            case 'rating':-->
<!--                items = response.ratings;-->
<!--                break;-->
<!--            case 'subscription':-->
<!--                items = response.subscriptions;-->
<!--                break;-->
            // 다른 카테고리에 대한 처리 추가
            default:
                items = [];
        }

        if (items && items.length > 0) {
            $('#activity-container').show();
            $('#empty-message').hide();

            items.forEach(function(item) {
                var element = createActivityElement(response.category, item);
                container.append(element);
            });

            // 필요한 경우 추가 이벤트 리스너 설정
            setFavoriteButtonListeners();

        } else {
            $('#activity-container').hide();
            $('#empty-message').show();
        }
    }

    function createActivityElement(category, item) {
        var element = $('<div>').addClass('activity-item');
        switch(category) {
            case 'favorite':
            case 'rating':

                console.log('favorite item: ', item);

                element.html(`
                    <img src="https://image.tmdb.org/t/p/w500${item.poster_path}"
                         alt="${item.title}"
                         onclick="navigateToMovieDetails(${item.tid})">
                    <p class="activity-title">${item.title}</p>
                    ${category === 'rating' ? `<p class="rating">평점: ${item.score}</p>` : ''}
                    <button id="favoriteButton-${item.id}"
                            data-activity-id="${item.id}"
                            class="favorite active">
                        <i class="fas fa-heart"></i>
                    </button>
                `);

                break;
<!--            case 'subscription':-->
<!--                element.html(`-->
<!--                    <img src="${item.profileImage}" alt="${item.nickname}">-->
<!--                    <p class="user-name">${item.nickname}</p>-->
<!--                `);-->
<!--                break;-->
            // 다른 카테고리에 대한 처리 추가
        }
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