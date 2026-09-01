package com.clinica.api.services;


import com.clinica.api.dto.externals.FeriadoResponse;
import com.clinica.api.exception.ServicoIndisponivelException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class FeriadoService {

    private static final String FERIADOS_URL = "https://brasilapi.com.br/api/feriados/v1/{ano}";
    private final RestTemplate restTemplate;

    public FeriadoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<FeriadoResponse> buscarFeriados(int ano){

        try {
            FeriadoResponse[] resposta = restTemplate.getForObject(
                    FERIADOS_URL,
                    FeriadoResponse[].class,
                    ano
            );

            return resposta != null
                    ? Arrays.asList(resposta)
                    : List.of();
        } catch (RestClientException ex) {
            throw new ServicoIndisponivelException("Serviço de consulta de feriado indisponivel no momento");
        }
    }

    public boolean ehFeriado(LocalDate data){

        List<FeriadoResponse> feriados = buscarFeriados(data.getYear());

        return feriados.stream().anyMatch(feriado -> feriado.getDate().equals(data));
    }

}
