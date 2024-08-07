package com.example.movie_review.movieDetail;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.movieDetail.DTO.ActorDetails;
import com.example.movie_review.movieDetail.DTO.MovieDTO;
import com.example.movie_review.movieDetail.DTO.MovieDetailDTO;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.movieDetail.service.MovieDetailDTOService;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.service.UserDTOService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Controller
@RequiredArgsConstructor
public class MovieDetailController {

    private final MovieDetailDTOService movieDetailDTOService;
    private final TmdbService tmdbService;
    private final UserDTOService userDTOService;
    private final MovieCommonDTOService movieCommonDTOService;

    private final ObjectMapper objectMapper;
    /**
     * 영화 상세 정보 보여주기
     * 해당 영화의 상세 정보를 가저 오면서 db에 해당 영화가 존재하는 지 확인
     * 만약 존재하지 않으면 db에 추가
     * 존재하면 db에 있는 거 그대로 반환
     */
    @GetMapping("/contents/{movieId}")
    public String movieDetail(@PathVariable Long movieId, Model model, Authentication principal) {
        try {
            MovieDetailDTO movieDetailDTO = movieDetailDTOService.getMovieDetailDTO(movieId, principal);
            List<ReviewDTO> sortedReviews = movieDetailDTO.getReviews().stream()
                    .sorted((r1, r2) -> Integer.compare(r2.getHeartCnt(), r1.getHeartCnt()))
                    .limit(6)
                    .collect(Collectors.toList());

            sortedReviews.forEach(reviewDTO -> {
                String reviewTextWithBreaks = reviewDTO.getReview().getText().replace("\n", "<br>");
                reviewDTO.getReview().setText(reviewTextWithBreaks);
            });

            model.addAttribute("sortedReviews", sortedReviews);
            model.addAttribute("movieDTO", movieDetailDTO);
        } catch (Exception e) {
            log.error("Error processing movie details", e);
            model.addAttribute("error", "영화 정보를 처리하는 중 오류가 발생했습니다.");
        }

        return "movieDetail";
    }

    @GetMapping("/people/{personId}")
    public String actorDetail(
            @PathVariable Long personId,
            @RequestParam String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        String actorDetailsJson = tmdbService.getPersonDetails(personId).block();

        // popularity sort, media_type = movie만 일단
        try {
            if (actorDetailsJson == null || actorDetailsJson.isEmpty()) {
                throw new Exception("Empty or null JSON data");
            }
            ActorDetails actorDetails = objectMapper.readValue(actorDetailsJson, ActorDetails.class);

            // 배우 담당
            if("cast".equals(type)) {
                List<ActorDetails.Cast> sortedCast = actorDetails.getCast().stream()
                        .filter(cast -> "movie".equals(cast.getMedia_type()))
                        .sorted((cast1, cast2) -> Double.compare(cast2.getPopularity(), cast1.getPopularity()))
                        .collect(Collectors.toList());

                int start = (page - 1) * size;
                int end = Math.min(start + size, sortedCast.size());
                List<ActorDetails.Cast> pagedCast = sortedCast.subList(start, end);

//                actorDetails.setCast(sortedCast);
                model.addAttribute("actorDetails", actorDetails);
                model.addAttribute("pagedCast", pagedCast);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", (int) Math.ceil((double) sortedCast.size() / size));
                return "actorDetail";
            }
            // 감독 담당
            else if("crew".equals(type)){
                Map<Integer, ActorDetails.Crew> crewMap = new HashMap<>();
                for (ActorDetails.Crew crew : actorDetails.getCrew()) {
                    if ("movie".equals(crew.getMedia_type())) {
                        int movieId = crew.getId();
                        if (crewMap.containsKey(movieId)) {
                            ActorDetails.Crew existingCrew = crewMap.get(movieId);
                            String combinedJobs = existingCrew.getJob() + "\n" + crew.getJob();
                            existingCrew.setJob(combinedJobs);
                        } else {
                            crewMap.put(movieId, crew);
                        }
                    }
                }

                List<ActorDetails.Crew> sortedCrew = crewMap.values().stream()
                        .sorted((crew1, crew2) -> Double.compare(crew2.getPopularity(), crew1.getPopularity()))
                        .collect(Collectors.toList());
                int start = (page - 1) * size;
                int end = Math.min(start + size, sortedCrew.size());
                List<ActorDetails.Crew> pagedCrew = sortedCrew.subList(start, end);

                model.addAttribute("directorDetails", actorDetails);
                model.addAttribute("pagedCrew", pagedCrew);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", (int) Math.ceil((double) sortedCrew.size() / size));

                return "directorDetail";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "jwt-login";
    }
}
