package org.netflix.encodingservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadedEvent {

    private String movieId;
    private String videoKey;
    private String bucketName;
    private String originalFileName;
    private Long fileSizeBytes;

}
