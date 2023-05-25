package com.linking.page.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
public class PageHandShakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

//        log.info("PageHandShakeInterceptor.beforeHandshake");
//
//        ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
//        HttpServletRequest servletRequest = serverRequest.getServletRequest();
//        HttpSession session = servletRequest.getSession(isCreateSession());
//
//        if (session == null) {
//            log.info("미인증 사용자 요청");
//            response.setStatusCode(HttpStatus.UNAUTHORIZED); // unauthenticated
//            return false;
//        }
//
//        UserCheck userCheck = (UserCheck) session.getAttribute(SessionConst.LOGIN_USER);
//
//        try {
//            Long projectId = Long.valueOf(servletRequest.getParameter("projectId"));
//            Long pageId =  Long.valueOf(servletRequest.getParameter("pageId"));
//
//            attributes.put("projectId", projectId);
//            attributes.put("pageId", pageId);
//            attributes.put("userId", userCheck.getUserId());
//
//        } catch (RuntimeException e) {
//            response.setStatusCode(HttpStatus.BAD_REQUEST);
//            return false;
//        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }


}
