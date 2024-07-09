package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;
import com.example.movie_review.genre.GenresRepository;
import com.example.movie_review.genre.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 해당 api 시작 전 데이터베이스에 모든 영화 정보를 기록하는 과정
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class AddMoviesData {

    private final GenresRepository genresRepo;
    private final GenreService genreService;
    private final GenresRepository genresRepository;

    private final MovieRepository movieRepo;
    private final Map<Long, Long> MovieIdToTid;

//    @GetMapping("/add-movies")
//    public ResponseEntity<?> addMovies() throws IOException {
//
//        File csv = new File("C:\\Users\\joeda\\Desktop\\spring\\movie_review\\ml-latest-small\\movies.csv");
//        BufferedReader br = new BufferedReader(new BufferedReader((new FileReader(csv))));
//
//        String line = "";
//        boolean skipFirstLine = true;
//        while ((line = br.readLine()) != null) {
//            if(skipFirstLine) {
//                skipFirstLine = false;
//                continue;
//            }
//
//            String[] token = line.split(",");
//            Long movieId = Long.parseLong(token[0]);
//            String[] genre = token[token.length - 1].split("\\|");
//
//            StringBuilder title = new StringBuilder();
//            for(int i=1; i<token.length -1; i++) {
//                title.append(token[i]);
//                if(i != token.length-2)
//                    title.append(",");
//            }
//
//            movieRepo.save(Movies.builder()
//                    .id(movieId).tId(MovieIdToTid.get(movieId))
//                    .title(title.toString())
//                    .genres(Arrays.stream(genre)
//                        .map(genreService::findOrCreateNew)
//                        .collect(Collectors.toSet()))
//                    .build());
//        }
//        return ResponseEntity.ok().build();
//    }

//    @GetMapping("/add-genres")
//    public void addGenres() {
//        List<Genres> genres = genreService.fetchGenres();
//        for (Genres genre : genres) {
//            genreService.findOrCreateNew(genre.getName(), genre.getId());
////            System.out.println("genre.getGenreId() = " + genre.getGenreId());
////            System.out.println("genre.getName() = " + genre.getName());
//        }
//    }
}
