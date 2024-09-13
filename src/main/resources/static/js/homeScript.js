function initializeMovieLists(popularMoviesJson, trendingMoviesJson) {
    try {
        var popularMovies = JSON.parse(popularMoviesJson);
        var trendingMovies = JSON.parse(trendingMoviesJson);

        if (popularMovies && popularMovies.results) {
            createMovieList(popularMovies.results, 'popularMovieList');
        } else {
            console.log('No popular movies data available');
        }
        if (trendingMovies && trendingMovies.results) {
            createMovieList(trendingMovies.results, 'trendingMovieList');
        } else {
            console.log('No trending movies data available');
        }
    } catch (error) {
        console.error('Error initializing movie lists:', error);
    }
}

function createMovieList(movies, containerId) {
    var movieListContainer = document.getElementById(containerId);

    movies.forEach(function(movie) {
        var li = document.createElement('li');
        li.classList.add('movie-item');

        var h3 = document.createElement('h3');
        h3.textContent = movie.title;

        var img = document.createElement('img');
        img.src = 'https://image.tmdb.org/t/p/w500' + movie.poster_path;
        img.alt = movie.title;
        img.addEventListener('click', function() {
            navigateToMovieDetails(movie.id);
        });

        var infoDiv = document.createElement('div');
        infoDiv.classList.add('movie-info');
        infoDiv.addEventListener('click', function() {
            toggleMovieInfo(infoDiv);
        });

        var pRating = document.createElement('p');
        pRating.textContent = '평균 평점: ' + movie.vote_average;

        var pReleaseDate = document.createElement('p');
        pReleaseDate.textContent = '개봉일: ' + movie.release_date;

        var pOverview = document.createElement('p');
        pOverview.textContent = movie.overview;

        infoDiv.appendChild(pRating);
        infoDiv.appendChild(pReleaseDate);
        infoDiv.appendChild(pOverview);

        li.appendChild(h3);
        li.appendChild(img);
        li.appendChild(infoDiv);
        movieListContainer.appendChild(li);
    });
}

function navigateToMovieDetails(movieId) {
    window.location.href = '/home/contents/' + movieId;
}

function toggleMovieInfo(infoDiv) {
    infoDiv.style.display = infoDiv.style.display === "none" ? "block" : "none";
}

function scrollContainer(containerId, direction) {
    const container = document.getElementById(containerId);
    const scrollAmount = 170;
    container.scrollBy({
        left: direction * scrollAmount,
        behavior: 'smooth'
    });
}
