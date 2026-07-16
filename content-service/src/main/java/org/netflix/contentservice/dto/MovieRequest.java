package org.netflix.contentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.netflix.contentservice.model.Genre;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Genre is required")
    private Genre genre;

    private String director;

    private String cast;

    private int releaseYear;

    private  double rating;

    private String thumbnailUrl;

    private int durationMinutes;

}
