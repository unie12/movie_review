package com.example.movie_review.movieDetail;

import com.example.movie_review.movieDetail.DTO.ActorDetails;
import com.example.movie_review.movieDetail.DTO.MovieDetailDTO;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.movieDetail.service.MovieDetailDTOService;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.review.service.ReviewDTOService;
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

import java.util.Comparator;
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
    private final ReviewDTOService reviewDTOService;

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
            List<ReviewDTO> sortedReviews = reviewDTOService.getSortedReviews(movieDetailDTO);

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
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        String actorDetailsJson = tmdbService.getPersonDetails(personId).block();

        // popularity sort, media_type = movie만 일단
        try {
            if (actorDetailsJson == null || actorDetailsJson.isEmpty()) {
                throw new Exception("Empty or null JSON data");
            }
            ActorDetails actorDetails = objectMapper.readValue(actorDetailsJson, ActorDetails.class);

            List<?> items;
            // 배우 담당
            if("cast".equals(type)) {
                items = actorDetails.getCast().stream()
                        .filter(cast -> "movie".equals(cast.getMedia_type()) && !cast.getCharacter().contains("Self"))
                        .sorted(Comparator.comparingDouble(ActorDetails.Cast::getPopularity).reversed())
                        .collect(Collectors.toList());
            } else if("crew".equals(type)){
                Map<Integer, ActorDetails.Crew> crewMap = new HashMap<>();
                for (ActorDetails.Crew crew : actorDetails.getCrew()) {
                    if ("movie".equals(crew.getMedia_type()) && !crew.getJob().equals("Thanks") && !crew.getJob().contains("Self")) {
                        crewMap.merge(crew.getId(), crew, (existing, newCrew) -> {
                            existing.setJob(existing.getJob() + "\n" + newCrew.getJob());
                            return existing;
                        });
                    }
                }
                    items = crewMap.values().stream()
                            .sorted(Comparator.comparingDouble(ActorDetails.Crew::getPopularity).reversed())
                            .collect(Collectors.toList());
            } else{
                    throw new IllegalArgumentException("Invalid type: " + type);
            }

            int totalItems = items.size();
            int totalPages = (int) Math.ceil((double) totalItems / size);
            page = Math.max(1, Math.min(page, totalPages));  // Ensure page is within bounds

            int start = (page - 1) * size;
            int end = Math.min(start + size, totalItems);
            List<?> pagedItems = items.subList(start, end);

            model.addAttribute("pagedItems", pagedItems);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("type", type);
            model.addAttribute("personId", personId);

            int maxPages = 5;
            int startPage = Math.max(1, page - maxPages / 2);
            int endPage = Math.min(totalPages, startPage + maxPages - 1);

            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "personDetail";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/home";
    }
}
