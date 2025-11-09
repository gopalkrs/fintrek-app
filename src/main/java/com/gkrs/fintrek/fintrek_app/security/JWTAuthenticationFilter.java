package com.gkrs.fintrek.fintrek_app.security;

import com.gkrs.fintrek.fintrek_app.entity.User;
import com.gkrs.fintrek.fintrek_app.services.JWTUtilService;
import com.gkrs.fintrek.fintrek_app.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public final JWTUtilService jwtUtilService;
    public final UserDetailsServiceImpl userDetailsServiceImpl;

    public JWTAuthenticationFilter(JWTUtilService jwtUtilService, UserDetailsServiceImpl userDetailsServiceImpl){
        this.jwtUtilService = jwtUtilService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        String path = request.getServletPath();

        if(path.startsWith("/api/users") || path.startsWith("/api/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("Unauthorized: Missing or invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7).trim();
        try {
            username = jwtUtilService.extractUsername(jwt);
        } catch (Exception e) {
            // ✅ Case 2: Invalid or malformed token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid or malformed JWT token\"}");
            return;
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userDetailsServiceImpl.loadUserByUsername(username);
            if(jwtUtilService.validateToken(jwt)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Expired or invalid token\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
