package com.linking.config;

import com.linking.document.controller.DocumentController;
import com.linking.document.controller.DocumentWsHandler;
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

    private final DocumentWsHandler documentWsHandler;
//    private final PageWsHandler pageWsHandler;
//    private final TodoWsHandler todoWsHandler;
//    private final ChatWsHandler chatWsHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(documentWsHandler, "/ws/documents")
//                .addInterceptors()
//                .setHandshakeHandler()
                .setAllowedOrigins("*");
    }
}
