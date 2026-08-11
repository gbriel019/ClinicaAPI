package com.clinica.api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Login
                        .requestMatchers("/auth/**").permitAll()

                        // Usuários
                        .requestMatchers(HttpMethod.GET, "/usuarios/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.POST, "/usuarios")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/usuarios/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**")
                        .hasRole("ADMIN")

                        // Pacientes
                        .requestMatchers(HttpMethod.GET, "/pacientes/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/pacientes")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.PUT, "/pacientes/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.DELETE, "/pacientes/**")
                        .hasRole("ADMIN")

                        // Medicos
                        .requestMatchers(HttpMethod.GET, "/medicos/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/medicos")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/medicos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/medicos/**")
                        .hasRole("ADMIN")

                        // Especialidades
                        .requestMatchers(HttpMethod.GET, "/especialidades/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/especialidades")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/especialidades/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/especialidades/**")
                        .hasRole("ADMIN")



                        // Demais endpoints
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

