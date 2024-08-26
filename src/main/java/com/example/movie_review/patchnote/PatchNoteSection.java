package com.example.movie_review.patchnote;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PatchNoteSection {
    private String title;
    private String imageUrl;
    private String content;

    public PatchNoteSection(String title, String imageUrl, String content) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
    }
}
