package com.example.movie_review.movieDetail.DTO;

import lombok.Data;

import java.util.List;

@Data
public class BoxOfficeResult {
    private BoxOffice boxOfficeResult;

    @Data
    public static class BoxOffice {
        private String boxofficeType;
        private String showRange;
        private String yearWeekTime;
        private List<DailyBoxOffice> dailyBoxOfficeList;
        private List<WeeklyBoxOffice> weeklyBoxOfficeList; // 추가된 부분
    }

    @Data
    public static class DailyBoxOffice {
        private String rnum;
        private String rank; // 박스오피스 순위
        private String rankInten; // 전일대비 순위 증감분
        private String rankOldAndNew; // 랭킹 신규진입여부 OLD, NEW
        private String audiCnt; // 해당일 관객수
        private String audiInten; // 전일 대비 관객수 증감분
        private String audiChange; // 전일 대비 관객수 증감 비율
        private String audiAcc; // 누적관객수

        private String movieCd;
        private String movieNm;
        private String openDt;
        private String salesAmt;
        private String salesShare;
        private String salesInten;
        private String salesChange;
        private String salesAcc;
        private String scrnCnt;
        private String showCnt;
    }

    @Data
    public static class WeeklyBoxOffice { // 추가된 클래스
        private String rnum;
        private String rank;
        private String rankInten;
        private String rankOldAndNew;
        private String movieCd;
        private String movieNm;
        private String openDt;
        private String salesAmt;
        private String salesShare;
        private String salesInten;
        private String salesChange;
        private String salesAcc;
        private String audiCnt;
        private String audiInten;
        private String audiChange;
        private String audiAcc;
        private String scrnCnt;
        private String showCnt;
    }
}


