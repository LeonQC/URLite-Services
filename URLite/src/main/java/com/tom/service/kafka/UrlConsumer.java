package com.tom.service.kafka;

import com.tom.dao.UrlRepository;
import com.tom.pojo.Url;
import com.tom.pojo.UrlEvent;
import com.tom.service.webSocket.WebSocketSessionManager;
import com.tom.utils.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class UrlConsumer {
    private final WebSocketSessionManager wsManager;
    private final Base62 base62;
    private final UrlRepository urlRepository;

    @KafkaListener(topics = "url-create", groupId = "short-url-group")
    public void consume(UrlEvent event) {
        if (event.getIndex() == -1) {
            sendWsEnd(event);
            return;
        }

        try {
            Url url = base62.generate(event.getLongUrl());
            System.out.println(event.getLongUrl() + " ======> " + url.getShortUrl());
            urlRepository.save(url);
            Thread.sleep(1000);
            sendWs(event, url);

        } catch (Exception e) {
            log.error("Failed to process url event: {}", e.getMessage(), e);
            // Optional: rewrite message if it failed.
            // handleFail(event);
        }
    }

    private void sendWs(UrlEvent event, Url url) {
        Map<String, Object> result = new HashMap<>();
        result.put("index", event.getIndex());
        result.put("longUrl", event.getLongUrl());
        result.put("shortUrl", url.getShortUrl());

        wsManager.send(event.getBatchId(), result);
    }

    private void sendWsEnd(UrlEvent event) {
        Map<String, Object> result = new HashMap<>();
        result.put("index", -1);
        result.put("longUrl", null);
        result.put("shortUrl", null);

        wsManager.send(event.getBatchId(), result);
    }
}
