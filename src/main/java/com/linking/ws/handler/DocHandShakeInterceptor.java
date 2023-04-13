package com.linking.ws.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

public class DocHandShakeInterceptor extends HttpSessionHandshakeInterceptor {

//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) throws Exception {
//
//        // 위의 파라미터 중, attributes 에 값을 저장하면 웹소켓 핸들러 클래스의 WebSocketSession에 전달된다
//        System.out.println("Before Handshake");
//        HttpSession session = null;
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
//            session = serverRequest.getServletRequest().getSession(false);
//
//        }
//        Long projectId = Long.valueOf(request.getURI().getQuery());
//        attributes.put("projectId", projectId);
//
//        session.setAttribute();
//
//        if (session != null) {
//            Enumeration<String> names = session.getAttributeNames();
//            while(names.hasMoreElements())
//        }
//
//        Enumeration<String> attributeNames = req.getSession().getAttributeNames();
//        while (attributeNames.hasMoreElements()) {
//            System.out.println("attributeNames = " + attributeNames.nextElement());
//        }
//
//
//        System.out.println(req.getSession().getAttributeNames());
//        String id = (String);
//        attributes.put("project", id);
//        System.out.println("HttpSession에 저장된 id:"+id);
//
//        return super.beforeHandshake(request, response, wsHandler, attributes);
//    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        System.out.println("After Handshake");

        super.afterHandshake(request, response, wsHandler, ex);
    }

}
