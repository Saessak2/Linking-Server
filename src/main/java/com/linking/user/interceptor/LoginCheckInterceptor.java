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

        HttpSession session = request.getSession(true);

        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            log.info("미인증 사용자 요청");
            response.sendError(400, "로그인 하세요.");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
