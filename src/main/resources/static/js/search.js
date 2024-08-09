let debounceTimer;

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
                console.log('query: ', query);
                fetchSuggestions(query, searchType);
            } else {
                searchSuggestions.innerHTML = '';
            }
        }, 300); // 300ms 디바운스
    });
});

function fetchSuggestions(query, searchType) {
    fetch(`api/search/suggestions?query=${encodeURIComponent(query)}&searchType=${searchType}`)
        .then(response => response.json())
        .then(data => {
            console.log('data: ', data);
            displaySuggestions(data);
        })
        .catch(error => console.error('Error:', error));
}

function displaySuggestions(suggestions) {
    const searchSuggestions = document.getElementById('searchSuggestions');
    searchSuggestions.innerHTML = '';
    suggestions.forEach(suggestion => {
        const div = document.createElement('div');
        div.textContent = suggestion;
        div.addEventListener('click', () => {
            document.querySelector('input[name="query"]').value = suggestion;
            searchSuggestions.innerHTML = '';
        });
        searchSuggestions.appendChild(div);
    });
}