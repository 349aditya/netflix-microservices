package org.netflix.streamingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.netflix.streamingservice.dto.StreamingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamingService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.s3.presigned-url-expiry}")
    private long presignedUrlExpiry;
    // Redis key for caching streaming URLs
    private final static String STREAMING_URL_CACHE_PREFIX = "streaming:url:";

    public StreamingResponse getStreamingUrl(String movieId, String playlistKey) {
        log.info("Getting streaming URL for movie: {}", movieId);

        String cacheKey = STREAMING_URL_CACHE_PREFIX + movieId;

        // Check redis Cache first
        String cachedUrl = redisTemplate.opsForValue().get(cacheKey);
        if (cachedUrl != null) {
            log.info("Returning cached streaming URL for movie: {}", movieId);
            return new StreamingResponse(movieId, cachedUrl,
                    "1080, 720, 480, 360", presignedUrlExpiry);
        }
        // Generate presigned URL from S3
        log.info("Generating new presigned URL for movie: {}", movieId);
        String presignedUrl = generatePresignedUrl(playlistKey);

        redisTemplate.opsForValue().set(
                cacheKey,
                presignedUrl,
                55,
                TimeUnit.MINUTES
        );

        log.info("Streaming URL generated and cached for movie: {}", movieId);

        return new StreamingResponse(
                movieId,
                presignedUrl,
                "1080p, 720p, 480p, 360p",
                presignedUrlExpiry
        );
    }

    private String generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presignedUrlExpiry))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url()
                .toString();
    }

    public void invalidateCache(String movieId) {
        String cacheKey = STREAMING_URL_CACHE_PREFIX + movieId;
        redisTemplate.delete(cacheKey);
        log.info("Streaming URL cache invalidated for movie: {}", movieId);
    }

    public String getSignedPlaylist(String movieId, String playlistPath) {
        // Get base path for this playlist
        String basePath = playlistPath.substring(0,
                playlistPath.lastIndexOf('/') + 1);

        // Read m3u8 content from S3
        String m3u8Content = readFromS3(playlistPath);

        // Rewrite each line that is a segment or playlist reference
        String signedContent = rewriteM3u8SignedUrls(
                m3u8Content, basePath);

        return signedContent;
    }

    private String readFromS3(String s3Key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        ResponseInputStream<GetObjectResponse> response =
                s3Client.getObject(request);

        return new BufferedReader(new InputStreamReader(response))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private String rewriteM3u8SignedUrls(
            String m3u8Content, String basePath) {

        StringBuilder rewritten = new StringBuilder();

        for (String line : m3u8Content.split("\\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                rewritten.append(line).append("\n");
                continue;
            }

            String fullKey = basePath + trimmed;
            String signedUrl = generatePresignedUrl(fullKey);

            rewritten.append(signedUrl).append("\n");
        }

        return rewritten.toString();
    }
}