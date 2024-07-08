document.getElementById('search-button').addEventListener('click', function(event) {
    event.preventDefault();
    const query = document.getElementById('query').value;
    if (query) {
        fetch(`/search-movies?query=${query}`)
            .then(response => response.json())
            .then(data => {
                const searchResults = document.getElementById('search-results');
                searchResults.innerHTML = ''; // 기존 검색 결과를 지움
                data.results.forEach(movie => {
                    const li = document.createElement('li');
                    li.textContent = movie.title;
                    li.dataset.movieId = movie.id;
                    li.addEventListener('click', function() {
                        addFavoriteMovie(movie.title, movie.id);
                    });
                    searchResults.appendChild(li);
                });
            });
    }
});

// 선호 영화 추가 함수
function addFavoriteMovie(movieTitle, movieId) {
    const favoriteMovies = document.getElementById('favorite-movies');
    const favoriteMoviesInput = document.getElementById('favorite-movies-input');
    const currentFavorites = favoriteMoviesInput.value ? favoriteMoviesInput.value.split(',') : [];

    // 선호 영화 중복 체크 및 최대 개수 제한
    if (favoriteMovies.children.length < 5 && !currentFavorites.includes(movieId.toString())) {
        const li = document.createElement('li');
        li.textContent = movieTitle;
        li.dataset.movieId = movieId;
        favoriteMovies.appendChild(li);

        currentFavorites.push(movieTitle);
        currentFavorites.push(movieId.toString());
        favoriteMoviesInput.value = currentFavorites.join(',');

        // localStorage에 저장
        localStorage.setItem('favoriteMovies', favoriteMoviesInput.value);
    } else {
        alert('최대 5개의 선호 영화를 선택할 수 있으며, 중복 선택은 불가능합니다.');
    }
}

// 페이지 로드 시 데이터베이스에서 선호 영화 목록을 가져와 초기화
document.addEventListener('DOMContentLoaded', function() {
    fetch('/favorite-movies')
        .then(response => response.json())
        .then(data => {
            const favoriteMoviesInput = document.getElementById('favorite-movies-input');
            const favoriteMovies = document.getElementById('favorite-movies');
            favoriteMovies.innerHTML = '';

            const currentFavorites = [];

            data.forEach(movie => {
                const li = document.createElement('li');
                li.textContent = movie.movieTitle;
                li.dataset.movieId = movie.movieId;
                favoriteMovies.appendChild(li);

                currentFavorites.push(movie.movieTitle);
                currentFavorites.push(movie.movieId);
            });

            favoriteMoviesInput.value = currentFavorites.join(',');
            localStorage.setItem('favoriteMovies', favoriteMoviesInput.value);
        });
});
const maxGenres = 5;
const favoriteGenres = document.getElementById('favorite-genres');
const favoriteGenresInput = document.getElementById('favorite-genres-input');

function updateFavoriteGenres(checkbox) {
    const genreId = checkbox.value;
    const genreName = checkbox.nextSibling.textContent;

    if (checkbox.checked) {
        if (favoriteGenres.children.length >= maxGenres) {
            alert('최대 5개의 선호 장르를 선택할 수 있습니다.');
            checkbox.checked = false;
            return;
        }
        addGenreToList(genreId, genreName);
    } else {
        removeGenreFromList(genreId);
    }

    updateHiddenInput();
}

function addGenreToList(genreId, genreName) {
    const existingGenre = favoriteGenres.querySelector(`li[data-genre-id="${genreId}"]`);
    if (!existingGenre) {
        const li = document.createElement('li');
        li.textContent = genreName;
        li.dataset.genreId = genreId;
        favoriteGenres.appendChild(li);
    }
}

function removeGenreFromList(genreId) {
    const genreToRemove = favoriteGenres.querySelector(`li[data-genre-id="${genreId}"]`);
    if (genreToRemove) {
        favoriteGenres.removeChild(genreToRemove);
    }
}

function updateHiddenInput() {
    const selectedGenres = Array.from(favoriteGenres.children).map(li => {
        return `${li.textContent},${li.dataset.genreId}`;
    });
    favoriteGenresInput.value = selectedGenres.join('|');
}

// 초기 선호 장르 목록 설정
document.addEventListener('DOMContentLoaded', function() {
    const checkedGenres = document.querySelectorAll('input[name="preferredGenres"]:checked');
    checkedGenres.forEach(genre => {
        addGenreToList(genre.value, genre.nextSibling.textContent);
    });
    updateHiddenInput();

    // 페이지 로드 시 DB에서 가져온 선호 장르 목록 초기화
    const userPreferredGenres = /*[[${userPreferredGenres}]]*/ [];
    userPreferredGenres.forEach(genre => {
        addGenreToList(genre.genreId, genre.genreName);
    });
    updateHiddenInput();
});
