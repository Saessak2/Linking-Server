package com.linking.user.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // Controller 실행 전, return false 이면 컨트롤러 요청을 하지 않음.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("LoginCheckInterceptor => {}", requestURI);
        if (requestURI.equals("/auth/login"))
            return true;

        HttpSession session = request.getSession(false);

//        if (session == null || session.getAttribute("LOGIN_USER") == null) {
        if (session == null) {
            log.info("미인증 사용자 요청");
            response.sendError(401); // unauthenticated
            return false;
        }
        return true;
    }
}
