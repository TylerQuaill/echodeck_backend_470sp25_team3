// This file configures Spring Security for the app.

package edu.uscb.csci470sp25_team3.echodeck_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.security.JwtAuthEntryPoint;
import edu.uscb.csci470sp25_team3.echodeck_backend.security.JwtAuthenticationFilter;
import edu.uscb.csci470sp25_team3.echodeck_backend.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final UserRepository userRepository;

    public SecurityConfig(JwtUtil jwtUtil, JwtAuthEntryPoint jwtAuthEntryPoint, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            	// Allow public access to auth endpoints
                .requestMatchers("/auth/register", "/auth/login", "/auth/guest").permitAll()

                // Allow public access to soundboard library and /api/sounds
                .requestMatchers(HttpMethod.GET, "/api/soundboard/library", "/api/soundboard/library/").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/sounds", "/api/sounds/**").permitAll()
                
                // Only allow signed in users to modify their soundboard
                .requestMatchers(HttpMethod.POST, "/api/soundboard/add/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/soundboard/remove/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/soundboard/my-sounds/**").authenticated()
                
                // Then everything else requires authentication
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .accessDeniedHandler(accessDeniedHandler())
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }
    // local + netlify
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // List of frontend URL's allowed to make API calls 
        config.setAllowedOrigins(List.of(
        		"http://localhost:5173",
        		"https://echodeck.netlify.app"));
        
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    // Custom response when the user is authenticated but dosen't have permission
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> error = new HashMap<>();
            error.put("status", 403);
            error.put("error", "Forbidden");
            error.put("message", "Access denied: Insufficient permissions");
            error.put("path", request.getRequestURI());

            new ObjectMapper().writeValue(response.getWriter(), error);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
