package com.tom.config.handler;

import com.tom.service.webSocket.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final WebSocketSessionManager wsManager;

    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("========> websocket connection established <=========");
        String batchId = getBatchId(session);
        wsManager.add(batchId, session);
    }

    private String getBatchId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        return query.split("=")[1];
    }
}
