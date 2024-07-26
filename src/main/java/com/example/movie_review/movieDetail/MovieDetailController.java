package com.example.movie_review.movieDetail;

import com.example.movie_review.review.ReviewDTO;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.service.UserDTOService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Controller
@RequiredArgsConstructor
public class MovieDetailController {

    private final MovieDetailDTOService movieDetailDTOService;
    private final TmdbService tmdbService;
    private final UserDTOService userDTOService;

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

            model.addAttribute("sortedReviews", sortedReviews);
            model.addAttribute("movieDTO", movieDetailDTO);
        } catch (Exception e) {
            log.error("Error processing movie details", e);
            model.addAttribute("error", "영화 정보를 처리하는 중 오류가 발생했습니다.");
        }

        return "movieDetail";
    }

    /**
     * @param movieTId
     * @param model
     * @return
     */
    @GetMapping("/contents/{movieTId}/reviews")
    public String movieReviews(@PathVariable Long movieTId, Model model) {
        model.addAttribute("movieTId", movieTId);
        return "movieReviews";
    }

    /**
     * 영화 배우 클릭 관련.. 향후 수정 필요
     */
    @GetMapping("/people/{actorId}")
    public String actorDetail(@PathVariable Long actorId, Model model) {
        String actorDetailsJson = tmdbService.getActorDetails(actorId).block();

        // popularity sort, media_type = movie만 일단
        try {
            if (actorDetailsJson == null || actorDetailsJson.isEmpty()) {
                throw new Exception("Empty or null JSON data");
            }
            ActorDetails actorDetails = objectMapper.readValue(actorDetailsJson, ActorDetails.class);

            List<ActorDetails.Cast> sortedCast = actorDetails.getCast().stream()
                    .filter(cast -> "movie".equals(cast.getMedia_type()))
                    .sorted((cast1, cast2) -> Double.compare(cast2.getPopularity(), cast1.getPopularity()))
                    .collect(Collectors.toList());
            actorDetails.setCast(sortedCast);
            model.addAttribute("actorDetails", actorDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "actorDetail";
    }
}
