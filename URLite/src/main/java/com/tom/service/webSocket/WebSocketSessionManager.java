package com.tom.service.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionManager {
    private final Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    public void add(String batchId, WebSocketSession session) {
        sessions.computeIfAbsent(batchId, k -> new ArrayList<>()).add(session);
    }

    public void send(String batchId, Object message) {
        List<WebSocketSession> list = sessions.get(batchId);
        if (list == null) return;

        list.forEach(session -> {
            try {
                session.sendMessage(
                        new TextMessage(new ObjectMapper().writeValueAsString(message))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
