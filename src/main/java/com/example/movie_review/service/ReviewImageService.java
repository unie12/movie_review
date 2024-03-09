package com.example.movie_review.service;

import com.example.movie_review.domain.review.Review;
import com.example.movie_review.domain.review.ReviewImage;
import com.example.movie_review.repository.ReviewImageRepository;
import com.example.movie_review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;
    private final String rootPath = System.getProperty("user.dir");
    private final String fileDir = rootPath + "/src/main/resources/static/review-images";

    /**
     * 사진 위치 찾기
     */
    public String getFulllPath(String filename) {
        return fileDir + filename;
    }

    /**
     * 사진 저장
     */
    public ReviewImage saveImage(MultipartFile multipartFile, Review review) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        // 원본 파일명 -> 서버에 저장된 파일명 (중복x)
        // 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
        String savedFilename = UUID.randomUUID() + "." + extractExt(originalFilename);

        // 파일 저장
        try {
            Path path = Paths.get(fileDir, savedFilename);
            Files.createFile(path);
            multipartFile.transferTo(path);
        } catch (Exception e) {
            System.out.println("create file failed");
        }

        return reviewImageRepository.save(ReviewImage.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .build());
    }

    /**
     * 사진 삭제
     */
    @Transactional
    public void deleteImage(ReviewImage reviewImage) throws IOException {
        reviewImageRepository.delete(reviewImage);
        Files.deleteIfExists(Paths.get(getFulllPath(reviewImage.getOriginalFilename())));
    }

    /**
     * 확장자 추출
     */
    private String extractExt(String originalFilename)
    {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    /**
     * 사진 다운로드
     */
    public ResponseEntity<UrlResource> downloadImage(Long reviewId) throws MalformedURLException {
        // reviewId에 해당하는 게시글이 없으면 null return
        Review review = reviewRepository.findById(reviewId).get();
        if(review == null || review.getReviewImage() == null) {
            return null;
        }

        UrlResource urlResource = new UrlResource("file:" + getFulllPath(review.getReviewImage().getSavedFilename()));

        // 업로드 한 파일명이 한글인 경우 한글 꺠지지 않기 위한 작업
        String encodedUploadFileName = UriUtils.encode(review.getReviewImage().getOriginalFilename(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        // header에 content_disposition 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}
