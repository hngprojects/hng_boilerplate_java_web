package hng_java_boilerplate.config;

import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final UserServiceImpl userService;
    private final JwtAuthenticationFilter authentication;

    public WebSecurityConfig(@Lazy UserServiceImpl userService, JwtAuthenticationFilter authentication) {
        this.userService = userService;
        this.authentication = authentication;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(httpRequests ->
                        httpRequests
                                .requestMatchers(
                                        "/",
                                        "/docs",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs",
                                        "/api/v1/products/**",
                                        "/swagger-ui/index.html",
                                        "/swagger-resources/**",
                                        "/webjars/**","/metrics",
                                        "/swagger-ui/**",
                                        "/api/v1/auth/**",
                                        "/api/v1/waitlist",
                                        "/api/v1/faqs",
                                        "/api/v1/contact-us",
                                        "/api/v1/squeeze/",
                                        "/api/v1/comments/**",
                                        "/api/v1/resources/articles",
                                        "/api/v1/resources/search",
                                        "/api/v1/resources/searchResources",
                                        "/api/v1/resources/create",
                                        "/api/v1/resources/edit",
                                        "/api/v1/resources/delete/{Id}",
                                        "/api/v1/resources/{Id}",
                                        "/api/v1/resources/unpublish/{id}",
                                        "/api/v1/resources/publish/{id}",
                                        "/api/v1/resources/published",
                                        "/api/v1/resources/unpublished",
                                        "/api/v1/regions",
                                        "/api/v1/testimonials/**",
                                        "api/v1/waitlist/**",
                                        "/api/v1/jobs/**",
                                        "/api/v1/notifications",
                                        "/api/v1/payment/plans",
                                        "/api/v1/payment/webhook",
                                        "/api/v1/notification-settings"
                                ).permitAll()
                                .requestMatchers(

                                        "/api/v1/auth/logout",
                                        "/api/v1/payment/stripe/**",
                                        "/api/v1/organisations/**",
                                        "/api/v1/accounts/**",
                                        "api/v1/auth/2fa/**").authenticated())
                .logout(logout -> logout
                        .deleteCookies("remove")
                        .invalidateHttpSession(true)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authentication, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
