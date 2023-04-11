package com.linking.config;

import com.linking.document.controller.DocumentController;
import com.linking.page.controller.PageController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final DocumentController documentController;
    private final PageController pageController;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(documentController, "/ws/documents")
                .addHandler(pageController, "/ws/pages")
//                .addInterceptors()
//                .setHandshakeHandler()
                .setAllowedOrigins("*");
    }
}
