let debounceTimer;
let currentFocus = -1;

document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.querySelector('input[name="query"]');
    const searchSuggestions = document.getElementById('searchSuggestions');
    const searchTypeSelect = document.getElementById('searchType');

    searchInput.addEventListener('input', function() {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            const query = this.value;
            const searchType = searchTypeSelect.value;
            if (query.length >= 2) { // 두글자 이상부터 검색 시작
                fetchSuggestions(query, searchType);
            } else {
                searchSuggestions.innerHTML = '';
            }
        }, 300); // 300ms 디바운스
    });

    searchInput.addEventListener('keydown', function(e) {
        const suggestions = searchSuggestions.getElementsByTagName('div');

        if (e.keyCode === 40) { // 아래쪽 화살표
            currentFocus++;
            addActive(suggestions);
        } else if (e.keyKode === 38) { // 위쪽 화살표
            currentFocus--;
            addActive(suggestions);
        } else if (e.keyCode === 13) { // 엔터
            if (currentFocus > -1) {
                if (suggestions[currentFocus]) {
                    suggestions[currentFocus].click();
                }
            }
        }
    });

    document.addEventListener('click', function(e) {
        if (e.target !== searchInput && e.target !== searchSuggestions) {
            searchSuggestions.innerHTML = '';
        }
    });
});

function fetchSuggestions(query, searchType) {
    fetch(`/api/search/suggestions?query=${encodeURIComponent(query)}&searchType=${searchType}`)
        .then(response => response.json())
        .then(data => {
            displaySuggestions(data);
        })
        .catch(error => console.error('Error:', error));
}

function displaySuggestions(suggestions) {
    const searchSuggestions = document.getElementById('searchSuggestions');
    searchSuggestions.innerHTML = '';
    suggestions.forEach((suggestion, index) => {
        const div = document.createElement('div');
        div.textContent = suggestion;
        div.addEventListener('click', () => {
            document.querySelector('input[name="query"]').value = suggestion;
            searchSuggestions.innerHTML = '';
        });
        div.addEventListener('mouseover', () => {
            currentFocus = index;
            addActive(searchSuggestions.getElementsByTagName('div'));

        });
        searchSuggestions.appendChild(div);
    });
}

function addActive(suggestions) {
    if (!suggestions) return false;
    removeActive(suggestions);
    if (currentFocus >= suggestions.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (suggestions.length - 1);
    suggestions[currentFocus].classList.add("active");
}

function removeActive(suggestions) {
    for (let i = 0; i < suggestions.length; i++) {
        suggestions[i].classList.remove("active");
    }
}