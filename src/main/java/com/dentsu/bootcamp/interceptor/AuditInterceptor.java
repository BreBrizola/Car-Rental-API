package com.dentsu.bootcamp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j(topic = "AUDIT")
@Component
public class AuditInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String remoteAddr = request.getRemoteAddr();
        LocalDateTime timestamp = LocalDateTime.now();

        log.info("Method: {}, URI: {}, QueryString: {}, RemoteAddress: {}, TimeStamp: {}",
                method, uri, queryString, remoteAddr, timestamp);

        return true;
    }
}
