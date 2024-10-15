package com.bookserver.deamon.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.bookserver.deamon.util.RateLimiter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiter rateLimiter;

    // 每分钟请求限制次数
    private static final int REQUEST_LIMIT = 100;
    private static final int TIME_WINDOW_SECONDS = 60;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String clientIp = request.getRemoteAddr(); // 获取客户端 IP
        boolean isAllowed = rateLimiter.isAllowed(clientIp, REQUEST_LIMIT, TIME_WINDOW_SECONDS);

        if (!isAllowed) {
            // 设置状态码 429 Too Many Requests
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
            return false;
        }

        // 允许请求继续
        return true;
    }
}
