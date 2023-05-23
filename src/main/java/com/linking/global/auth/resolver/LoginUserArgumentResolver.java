package com.linking.global.auth.resolver;

import com.linking.global.common.Login;
import com.linking.global.common.SessionConst;
import com.linking.global.common.UserCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    // resolveArgument를 실행하기 위한 조건
    // @Login 어노테이션 존재 && UserCheck 타입이어야 한다.
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasUserType = UserCheck.class.isAssignableFrom(parameter.getParameterType());
        log.info("hasLoginAnnotation => {}, hasUserType => {}", hasLoginAnnotation, hasUserType);
        return hasLoginAnnotation && hasUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null)
            return null;
        return session.getAttribute(SessionConst.LOGIN_USER);
    }
}
