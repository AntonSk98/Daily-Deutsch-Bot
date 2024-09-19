package com.ansk.development.learngermanwithansk98.rest;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Filter to authenticate access to the API.
 *
 * @author Anton Skripin
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Value("${rest.internal.security.username}")
    private String username;

    @Value("${rest.internal.security.password}")
    private String password;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Basic ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization header is invalid or missing");
            return;
        }

        try {
            String base64Credentials = StringUtils.substringAfter(authorizationHeader, "Basic ").trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            String[] userNamePassword = StringUtils.split(credentials, ":", 2);

            String receivedUsername = userNamePassword[0];
            String receivedPassword = userNamePassword[1];

            if (!StringUtils.equals(username, receivedUsername) || !StringUtils.equals(password, receivedPassword)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials");
                return;
            }

            
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid authorization format");
        }
    }
}
