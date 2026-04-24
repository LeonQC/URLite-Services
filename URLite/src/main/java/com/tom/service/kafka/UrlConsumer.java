package com.tom.service.kafka;

import com.tom.pojo.UrlEvent;
import com.tom.service.webSocket.WebSocketSessionManager;
import com.tom.utils.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class UrlConsumer {
    private final WebSocketSessionManager wsManager;

    @KafkaListener(topics = "url-create", groupId = "short-url-group")
    public void consume(UrlEvent event) {
        // Avoid null transfer to shortUrl.
        if (event.getIndex() == -1) {
            Map<String, Object> result = new HashMap<>();
            result.put("index", event.getIndex());
            result.put("longUrl", null);
            result.put("shortUrl", null);
            wsManager.send(event.getBatchId(), result);
        } else {
            String shortUrl = Base62.generate(event.getLongUrl());
            System.out.println(event.getLongUrl() + ":========>:" + shortUrl);
            Map<String, Object> result = new HashMap<>();
            result.put("index", event.getIndex());
            result.put("longUrl", event.getLongUrl());
            result.put("shortUrl", shortUrl);

            wsManager.send(event.getBatchId(), result);
        }
    }
}
