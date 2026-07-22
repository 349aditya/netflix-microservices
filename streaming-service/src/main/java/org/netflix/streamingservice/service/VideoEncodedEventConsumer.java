package org.netflix.streamingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.netflix.streamingservice.event.VideoEncodedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoEncodedEventConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String MASTER_PLAYLIST_KEY_PREFIX = "streaming:playlist:";

    @KafkaListener(
            topics = "video.encoded",
            groupId = "streaming-service-group"
    )
    public void consumeVideoEncodedEvent(VideoEncodedEvent event) {
        log.info("Consumed VideoEncodedEvent for movie: {} success: {}",
                event.getMovieId(), event.isSuccess());

        if(event.isSuccess()) {
            // Store master playlist key in redis
            String cacheKey = MASTER_PLAYLIST_KEY_PREFIX + event.getMovieId();
            redisTemplate.opsForValue().set(cacheKey, event.getMasterPlaylistKey());
            log.info("Master playlist key stored in Redis for movie: {}", event.getMovieId());
        }
        else {
            log.error("Encoding failed for movie: {} - {}",
                    event.getMovieId(), event.getErrorMessage());
        }
    }
}
