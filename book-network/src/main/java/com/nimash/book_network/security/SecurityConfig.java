package com.nimash.book_network.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .cors(withDefaults()) // cross-platform
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req->
                        req.requestMatchers(
                                "/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                ).permitAll()//These paths are accessible to everyone (no authentication required).
                                        .anyRequest()
                                        .authenticated()//.anyRequest().authenticated() //Any other request must be authenticated.

                )
                .sessionManagement(session->session.sessionCreationPolicy(STATELESS))
                //STATELESS means that the application will not store user sessions.
                //Useful for JWT-based authentication, where the client must send the token with every request.
                .authenticationProvider(authenticationProvider)
                //Specifies a custom AuthenticationProvider that handles authentication logic.
                //Usually, this provider checks JWT tokens or credentials against a database.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                //Adds a JWT filter before UsernamePasswordAuthenticationFilter.
                //The jwtAuthFilter extracts and validates JWT tokens from requests.
        return  http.build();
    }


}
