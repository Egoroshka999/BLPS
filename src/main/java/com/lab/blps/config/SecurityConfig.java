package com.lab.blps.config;
import com.lab.blps.security.*;
import com.lab.blps.utils.Sha512PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable)
                 .cors(cors -> cors.configurationSource(request -> {
                     var corsConfiguration = new CorsConfiguration();
                     corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5000"));
                     corsConfiguration.setAllowedMethods(List.of(
                             "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD", "CONNECT", "OPTIONS")
                     );
                     corsConfiguration.setAllowedHeaders(List.of("*"));
                     corsConfiguration.setAllowCredentials(true);
                     corsConfiguration.setMaxAge(10L);
                     corsConfiguration.addExposedHeader("X-Response-Uuid");
                     corsConfiguration.addExposedHeader("X-Total-Count");
                     return corsConfiguration;
                 }))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request -> {
                            request
                                    .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/api/developer/**").hasRole("DEVELOPER")
                                    .requestMatchers("/api/moderator/**").hasRole("MODERATOR")
                                    .requestMatchers("/api/financier/**").hasRole("FINANCIER")
                                    .anyRequest().authenticated();
                        }
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "sha512";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new Sha512PasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

}
