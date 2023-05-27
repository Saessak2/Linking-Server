package com.linking.global.config;

import com.linking.socket.page.handler.PageWebSocketHandler;
import com.linking.socket.interceptor.PageHandShakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class PageWebSocketConfig implements WebSocketConfigurer {

    private final PageWebSocketHandler pageWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(pageWebSocketHandler, "/ws/pages")
                .addInterceptors(new PageHandShakeInterceptor())
                .setAllowedOrigins("*");
    }
}
