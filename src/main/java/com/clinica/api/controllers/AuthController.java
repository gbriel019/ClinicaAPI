package com.clinica.api.controllers;

import com.clinica.api.dto.request.LoginRequest;
import com.clinica.api.dto.request.RefreshTokenRequest;
import com.clinica.api.dto.response.LoginResponse;
import com.clinica.api.config.security.JwtService;
import com.clinica.api.entities.Usuario;
import com.clinica.api.services.UsuarioService;

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

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UsuarioService usuarioService, CustomUserDetailsService customUserDetailsService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.customUserDetailsService = customUserDetailsService;
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        // Busca o usuário e verifica se ele está bloqueado antes de tentar autenticar.
        // Vale tanto para ADMIN quanto para RECEPCIONISTA
        Usuario usuario = usuarioService.buscarPorEmail(request.getEmail());
        usuarioService.validarBloqueio(usuario);

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getSenha()
                            )
                    );

            // Senha correta zera o contador de tentativas e qualquer bloqueio.
            usuarioService.registrarLoginSucesso(usuario);

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String accessToken = jwtService.gerarAccessToken(userDetails);
            String refreshToken = jwtService.gerarRefreshToken(userDetails);

            return ResponseEntity.ok(
                    new LoginResponse(accessToken, refreshToken)
            );

        } catch (BadCredentialsException ex) {
            // Senha incorreta inicia o contador e se chegar em 3, bloqueia.
            usuarioService.registrarTentativaFalha(usuario);
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