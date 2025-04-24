// This filter intercepts every HTTP request to check for a valid JWT token and authenticate the user if needed

package edu.uscb.csci470sp25_team3.echodeck_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Get the token
        String token = jwtUtil.getTokenFromRequest(request);

        // If no token, skip authentication and continue
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract email from the token
        String email = jwtUtil.extractEmail(token);

        // Find the user by email
        User user = userRepository.findByEmail(email).orElse(null);

        // If the user exists and the token is valid, authenticate the request
        if (user != null && jwtUtil.isTokenValid(token, email)) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user, null, List.of()); // Add authorities if needed
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
