const LoadingManager = {
    show: function(minTime = 500) { // 0.5초
        const loadingScreen = document.createElement('div');
        loadingScreen.id = 'loading-screen';
        loadingScreen.className = 'loading-screen';
        loadingScreen.innerHTML = `
            <div class="loading-animation">
                <img src="/images/loading-film.png" alt="Loading...">
            </div>
        `;
        document.body.appendChild(loadingScreen);

        this.startTime = Date.now();
        this.minTime = minTime;
    },

    hide: function() {
        const loadingScreen = document.getElementById('loading-screen');
        if (loadingScreen) {
            const elapsedTime = Date.now() - this.startTime;
            const remainingTime = Math.max(0, this.minTime - elapsedTime);

            setTimeout(() => {
                loadingScreen.remove();
            }, remainingTime);
        }
    },

    waitForImages: function() {
        const images = document.querySelectorAll('img');
        let loadedImages = 0;

        return new Promise((resolve) => {
            function imageLoaded() {
                loadedImages++;
                if (loadedImages == images.length) {
                    resolve();
                }
            }

            if (images.length === 0) {
                resolve();
            } else {
                images.forEach(img => {
                    if (img.complete) {
                        imageLoaded();
                    } else {
                        img.addEventListener('load', imageLoaded);
                        img.addEventListener('error', imageLoaded);
                    }
                });
            }
        });
    }
};