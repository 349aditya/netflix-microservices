package org.netflix.contentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.netflix.contentservice.model.Genre;
import org.netflix.contentservice.model.VideoStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {

    private String id;
    private String title;
    private String description;
    private Genre genre;
    private String director;
    private String cast;
    private int releaseYear;
    private double rating;
    private String thumbnailUrl;
    private int durationMinutes;
    private String videoKey;
    private String hlsUrl;
    private VideoStatus videoStatus;

}
