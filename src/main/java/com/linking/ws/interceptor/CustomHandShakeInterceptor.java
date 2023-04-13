package com.linking.ws.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class CustomHandShakeInterceptor extends HttpSessionHandshakeInterceptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {


        logger.info("beforeHandshake()");
        String param = request.getURI().getQuery();
        String str = null;

        try {
            if (param.contains("projectId=")) {  // ws documents
                str = param.replace("projectId=", "");
                long id = Long.parseLong(str);
                attributes.put("projectId", id);

            } else {
                logger.error("Uri not contains necessary params");
            }

        } catch (NumberFormatException e) {
            logger.error("LongParse cannot parse string => {}", str);
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
