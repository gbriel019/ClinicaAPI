package com.clinica.api.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_TENTATIVAS = 3;
    private static final long MINUTOS_BLOQUEIO = 15;

    private final Map<String, Integer> tentativas = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> bloqueios = new ConcurrentHashMap<>();

    public void verificarBloqueio(String ip) {

        LocalDateTime bloqueadoAte = bloqueios.get(ip);

        if (bloqueadoAte == null) {
            return;
        }

        if (bloqueadoAte.isAfter(LocalDateTime.now())) {
            throw new RuntimeException(
                    "IP bloqueado. Tente novamente após " + bloqueadoAte
            );
        }

        bloqueios.remove(ip);
        tentativas.remove(ip);
    }

    public void registrarFalha(String ip) {

        int quantidade = tentativas.getOrDefault(ip, 0) + 1;

        if (quantidade >= MAX_TENTATIVAS) {

            tentativas.remove(ip);

            bloqueios.put(
                    ip,
                    LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEIO)
            );

            return;
        }

        tentativas.put(ip, quantidade);
    }

    public void registrarSucesso(String ip) {
        tentativas.remove(ip);
        bloqueios.remove(ip);
    }
}