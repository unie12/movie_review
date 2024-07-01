package com.example.movie_review.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public ResponseEntity retrieveTags(Pageable pageable) {
        Page<Tags> tagsPage = tagRepository.findAll(pageable);
        return new ResponseEntity<>(tagsPage, HttpStatus.OK);
    }
}
