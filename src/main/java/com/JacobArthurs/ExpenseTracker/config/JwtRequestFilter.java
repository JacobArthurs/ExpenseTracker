package com.JacobArthurs.ExpenseTracker.config;

import com.JacobArthurs.ExpenseTracker.service.UserService;
import com.JacobArthurs.ExpenseTracker.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserService userService;

    @Autowired
    public JwtRequestFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            var jwt = authorizationHeader.substring(7);
            var isValidToken = JwtTokenUtil.validateToken(jwt);

            if (isValidToken) {
                var user = userService.loadUserByUsername(JwtTokenUtil.getUsernameFromToken(jwt));

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired or invalid");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}