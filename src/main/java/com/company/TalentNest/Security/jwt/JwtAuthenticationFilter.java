package com.company.TalentNest.security.jwt;

import com.company.TalentNest.Security.CustomUserDetailsService;
import com.company.TalentNest.Security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        System.out.println("Processing request: " + requestPath); // ✅ Debug log
        if (requestPath.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = getJwtFromRequest(request);
        System.out.println("JWT: " + jwt); // ✅ Debug log

        if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
            try {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                System.out.println("User ID from token: " + userId); // ✅ Debug log
                UserDetails userDetails = userDetailsService.loadUserById(userId);
                System.out.println("UserDetails: " + userDetails.getUsername() + ", Authorities: " + userDetails.getAuthorities()); // ✅ Debug log

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication set in SecurityContext"); // ✅ Debug log
            } catch (Exception e) {
                System.out.println("Error processing JWT: " + e.getMessage()); // ✅ Debug log
            }
        } else {
            System.out.println("Invalid or missing JWT token"); // ✅ Debug log
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}