package com.example.movie_review.patchnote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchNote {
    private String version;
    private LocalDate releaseDate;
    private LocalDate lastModified;
    private String title;
    private String thumbnailUrl;

    private List<PatchNoteSection> sections;
}
