window.activityHandlers = window.activityHandlers || {};
window.activityHandlers.subscriber = {
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

        return $('<div>').addClass('subs-card').html(`
            <a href="/info/${item.userCommonDTO.email}">
                <img src="${item.userCommonDTO.picture}" alt="${item.userCommonDTO.nickname}" class="subs-picture">
            </a>
            <div class="subs-info">
                <div class="subs-nickname">${item.userCommonDTO.nickname}</div>
                <div class="subs-cnt">구독자 ${item.subscriptionCnt}명</div>
                <div class="subs-date">구독일 ${this.formatDate(item.subscriptionDate)}</div>
            </div>
        `);
    },
    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric' });
    }
}