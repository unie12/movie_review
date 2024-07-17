package com.example.movie_review.genre;

import com.example.movie_review.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {
    private final GenresRepository genresRepository;

    /**
     * tmdb api
     */
    private final String TMDB_API_KEY = "c2b9766d219d3e785cfa2ff128b610ab";
    private final String TMDB_GENRE_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + TMDB_API_KEY + "&language=ko-KR";

    public List<Genres> fetchGenres() {
        RestTemplate restTemplate = new RestTemplate();
        GenreResponse response = restTemplate.getForObject(TMDB_GENRE_URL, GenreResponse.class);
        return response.getGenres();
    }

    public List<Genres> getAllGenres() {
        return genresRepository.findAll();
    }

    public Genres getGenreById(Long genreId) {
        return genresRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Email"));
    }

}

class GenreResponse {
    private List<Genres> genres;

    public List<Genres> getGenres() {
        return this.genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }
}
