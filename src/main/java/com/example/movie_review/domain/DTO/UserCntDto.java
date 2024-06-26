package com.example.movie_review.domain.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCntDto {

    private Long totalUserCnt;
    private Long totalAdminCnt;
    private Long totalBronzeCnt;
    private Long totalGoldCnt;
    private Long totalDiamondCnt;
    private Long totalBlacklistCnt;

}
