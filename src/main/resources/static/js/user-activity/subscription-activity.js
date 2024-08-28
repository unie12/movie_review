window.activityHandlers = window.activityHandlers || {};
window.activityHandlers.subscription = {
    updateActivityList: function(response, append) {
        var container = $('#activity-list');
        if (!append) {
            container.empty();
        }
        var items = response.subscriptionDTOs || [];

        if (items && items.length > 0) {
            $('#activity-container').show();
            $('#empty-message').hide();

            items.forEach((item) => {
                var element = this.createActivityElement(item);
                container.append(element);
            });

        } else {
            $('#activity-container').hide();
            $('#empty-message').show();
        }
    },

    createActivityElement: function(item) {
        var userRoleClass = item.userCommonDTO.role;

        return $('<div>').addClass('user-info').html(`
            <a href="/info/${item.userCommonDTO.email}">
                <img src="${item.userCommonDTO.picture}" alt="${item.userCommonDTO.nickname}" class="user-picture">
            </a>
            <p class="subscription-nickname ${userRoleClass}">${item.userCommonDTO.nickname}</p>
        `);
    }
}