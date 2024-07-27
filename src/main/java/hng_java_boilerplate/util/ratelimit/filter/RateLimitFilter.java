package hng_java_boilerplate.util.ratelimit.filter;

import hng_java_boilerplate.util.ratelimit.service.RateLimitService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;


public class RateLimitFilter implements Filter {
    private final RateLimitService rateLimitService;

    @Autowired
    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String clientIp = httpRequest.getRemoteAddr();

        if (rateLimitService.isAllowed(clientIp)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Rate limit exceeded. Try again later.");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
