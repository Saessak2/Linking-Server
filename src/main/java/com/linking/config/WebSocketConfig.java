package com.linking.config;

import com.linking.ws.handler.DocumentWebSocketHandler;
import com.linking.ws.handler.PageWebSocketHandler;
import com.linking.ws.interceptor.CustomHandShakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Map;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {


    private final DocumentWebSocketHandler documentWebSocketHandler;
    private final PageWebSocketHandler pageWebSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(documentWebSocketHandler, "/ws/documents")
                .addHandler(pageWebSocketHandler, "/ws/page")
                .addInterceptors(new CustomHandShakeInterceptor())
                .setAllowedOrigins("*");

    }
}
