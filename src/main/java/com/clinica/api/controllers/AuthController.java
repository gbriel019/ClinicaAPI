package com.clinica.api.controllers;

import com.clinica.api.dto.request.LoginRequest;
import com.clinica.api.dto.request.RefreshTokenRequest;
import com.clinica.api.dto.response.LoginResponse;
import com.clinica.api.config.security.JwtService;
import com.clinica.api.entities.Usuario;
import com.clinica.api.services.LoginAttemptService;
import com.clinica.api.services.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.clinica.api.config.security.CustomUserDetailsService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final CustomUserDetailsService customUserDetailsService;
    private final LoginAttemptService loginAttemptService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UsuarioService usuarioService, CustomUserDetailsService customUserDetailsService, LoginAttemptService loginAttemptService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.customUserDetailsService = customUserDetailsService;
        this.loginAttemptService = loginAttemptService;
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();

        loginAttemptService.verificarBloqueio(ip);

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getSenha()
                            )
                    );

            loginAttemptService.registrarSucesso(ip);

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String accessToken =
                    jwtService.gerarAccessToken(userDetails);

            String refreshToken =
                    jwtService.gerarRefreshToken(userDetails);

            return ResponseEntity.ok(
                    new LoginResponse(accessToken, refreshToken)
            );

        } catch (BadCredentialsException ex) {

            loginAttemptService.registrarFalha(ip);

            throw ex;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadCredentialsException("Token informado não é um refresh token");
        }

        String email = jwtService.extrairUsername(refreshToken);

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(email);

        if (!jwtService.tokenValido(refreshToken, userDetails)) {
            throw new BadCredentialsException("Refresh token inválido ou expirado");
        }

        String novoAccessToken =
                jwtService.gerarAccessToken(userDetails);

        return ResponseEntity.ok(
                new LoginResponse(novoAccessToken, refreshToken)
        );
    }

}