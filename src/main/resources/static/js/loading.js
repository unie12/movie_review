const LoadingManager = {
    show: function(minTime = 1200) {
        if (!document.getElementById('loading-screen')) {

            const loadingScreen = document.createElement('div');
            loadingScreen.id = 'loading-screen';
            loadingScreen.className = 'loading-screen';
            loadingScreen.innerHTML = `
                <div class="loading-animation">
                    <img src="/images/loading-film.png" alt="Loading...">
                </div>
            `;
            document.body.appendChild(loadingScreen);
        }
        this.startTime = Date.now();
        this.minTime = minTime;
        localStorage.setItem('loadingStartTime', this.startTime);
    },

    hide: function() {
        const loadingScreen = document.getElementById('loading-screen');
        if (loadingScreen) {
            const startTime = parseInt(localStorage.getItem('loadingStartTime') || '0');
            const elapsedTime = Date.now() - this.startTime;
            const remainingTime = Math.max(0, this.minTime - elapsedTime);

            setTimeout(() => {
                loadingScreen.remove();
                localStorage.removeItem('loadingStartTime');
            }, remainingTime);
        }
    },

    waitForImages: function(maxWaitTime = 2500) {
        const images = document.querySelectorAll('img');
        let loadedImages = 0;

        return new Promise((resolve) => {
            const timeout = setTimeout(() => {
                resolve();
            }, maxWaitTime);

            function imageLoaded() {
                loadedImages++;
                if (loadedImages == images.length) {
                    clearTimeout(timeout);
                    resolve();
                }
            }

            if (images.length === 0) {
                clearTimeout(timeout);
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
