package hng_java_boilerplate.util;

import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private  JwtUtils utils;
    private final UserServiceImpl userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils utils, @Lazy UserServiceImpl userService, HandlerExceptionResolver handlerExceptionResolver) {
        this.utils = utils;
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {



        String authenticationHeader = request.getHeader("Authorization");


        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

            String token = authenticationHeader.substring(7);
                String username = utils.extractUsername.apply(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserByUsername(username);

                    if (userDetails != null && utils.isTokenValid.apply(token, userDetails.getUsername())) {
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                        SecurityContextHolder.setContext(securityContext);
                    }
                }
                filterChain.doFilter(request, response);
    }
}
