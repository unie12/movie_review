package com.example.movie_review.patchnote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchNote {
    private String version;
    private LocalDate releaseDate;
    private LocalDate lastModified;
    private String title;
    private String content;
}
