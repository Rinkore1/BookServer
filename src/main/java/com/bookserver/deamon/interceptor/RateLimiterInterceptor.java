package com.bookserver.deamon.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.bookserver.deamon.util.RateLimiter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器类，用于限制客户端的请求速率。
 * 该拦截器使用 {@link RateLimiter} 来限制每个客户端在指定时间窗口内的请求次数。
 */
@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiter rateLimiter;

    // 每分钟请求限制次数
    private static final int REQUEST_LIMIT = 100;
    private static final int TIME_WINDOW_SECONDS = 60;

    /**
     * 在请求处理之前调用的方法。
     * 该方法检查客户端的请求是否超过了限制的次数，如果超过，则返回 HTTP 429 状态码。
     *
     * @param request  当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @param handler  处理器对象
     * @return 如果请求被允许则返回 true，否则返回 false
     * @throws Exception 如果在处理请求时发生错误
     */
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