function updateActivityList(response) {
    var container = $('#activity-list');
    container.empty();

    var items = response.subscriptionDTOs || [];

    if (items && items.length > 0) {
        $('#activity-container').show();
        $('#empty-message').hide();

        items.forEach(function(item) {
            var element = createActivityElement(item);
            container.append(element);
        });

    } else {
        $('#activity-container').hide();
        $('#empty-message').show();
    }
}

function createActivityElement(item) {
    console.log('Subscription: ', item);
    return $('<div>').addClass('user-info').html(`
        <a href="/info/${item.userCommonDTO.email}">
            <img src="${item.userCommonDTO.picture}" alt="${item.userCommonDTO.nickname}" class="user-picture">
        </a>
        <p class="user-name">${item.userCommonDTO.nickname}</p>
    `);
}