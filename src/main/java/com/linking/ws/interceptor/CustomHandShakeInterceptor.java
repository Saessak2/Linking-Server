package com.linking.ws.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

public class CustomHandShakeInterceptor extends HttpSessionHandshakeInterceptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        logger.info("beforeHandshake()");

        ServletServerHttpRequest ssreq = (ServletServerHttpRequest) request;
        HttpServletRequest req =  ssreq.getServletRequest();

        String uri = req.getRequestURI();

        try {
            if (uri.equals("/ws/documents")) {
                String projectId = req.getParameter("projectId");
                String userId = req.getParameter("userId");
                if (projectId != null && userId != null) {
                    attributes.put("projectId", Long.parseLong(projectId));
                    attributes.put("userId", Long.parseLong(userId));
                }
            } else if (uri.equals("/ws/page")) {
                String projectId = req.getParameter("projectId");
                String userId = req.getParameter("userId");
                String pageId = req.getParameter("pageId");
                if (projectId != null && userId != null && pageId != null) {
                    attributes.put("projectId", Long.parseLong(projectId));
                    attributes.put("userId", Long.parseLong(userId));
                    attributes.put("pageId", Long.parseLong(pageId));
                }
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        logger.info("afterHandshake()");

        super.afterHandshake(request, response, wsHandler, ex);
    }
}
