package com.clinica.api.config.security;

import jakarta.servlet.http.HttpServletResponse;
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

                        // LOGIN
                        .requestMatchers("/auth/**", "/health").permitAll()


                        // USUÁRIOS
                        .requestMatchers(HttpMethod.GET, "/usuarios/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.POST, "/usuarios")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/usuarios/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**")
                        .hasRole("ADMIN")


                        // PACIENTES
                        .requestMatchers(HttpMethod.GET, "/pacientes/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/pacientes")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.PUT, "/pacientes/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.DELETE, "/pacientes/**")
                        .hasRole("ADMIN")


                        // MÉDICOS
                        .requestMatchers(HttpMethod.GET, "/medicos/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/medicos")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/medicos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/medicos/**")
                        .hasRole("ADMIN")


                        // ESPECIALIDADES
                        .requestMatchers(HttpMethod.GET, "/especialidades/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/especialidades")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/especialidades/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/especialidades/**")
                        .hasRole("ADMIN")


                        // CONSULTAS
                        .requestMatchers(HttpMethod.GET, "/consultas/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/consultas")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.PUT, "/consultas/*/cancelar")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.GET, "/consultas/disponibilidade/*")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")


                        // ACTUATOR
                        .requestMatchers("/actuator/health", "/actuator/info")
                        .permitAll()


                        // SWAGGER
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        )
                        .permitAll()


                        // OUTROS ENDPOINTS
                        .anyRequest().authenticated()
                )

                // teste
                .exceptionHandling(handling -> handling

                        // teste 401
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        "Token ausente, inválido ou expirado"
                                )
                        )

                        // teste 403
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(
                                        HttpServletResponse.SC_FORBIDDEN,
                                        "Sem permissão para este recurso"
                                )
                        )
                )

                // JWT FILTER
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