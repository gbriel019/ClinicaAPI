package com.clinica.api.services;

import com.clinica.api.config.RestTemplate;
import com.clinica.api.dto.externals.ViaCepResponse;
import com.clinica.api.exception.BadRequestException;
import com.clinica.api.exception.NotFound;
import com.clinica.api.exception.ServicoIndisponivelException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;


@Service
public class ViaCepService {

    private static final Logger log = LoggerFactory.getLogger(ViaCepService.class);
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    private final RestTemplate restTemplate;

    private ViaCepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ViaCepResponse buscarEndereco(String cepBruto) {
        String cep = validarEFormatarCep(cepBruto);

        ViaCepResponse resposta;
        try {
            resposta = restTemplate.getForObject(VIA_CEP_URL, ViaCepResponse.class, cep);
        } catch (RestClientException ex) {
            log.error("Falha ao consultas ViaCep para o CEP {}:  {}", cep, ex.getMessage());
            throw new ServicoIndisponivelException("Serviço de consulta de CEP indisponivel no momento");
        }

        if (resposta == null || Boolean.TRUE.equals((resposta.getErro()))) {
            throw new NotFound("CEP não encontrado");
        }
        return resposta;
    }

    private String validarEFormatarCep(String cepBruto) {
        if (cepBruto == null) {
            throw new BadRequestException("O campo CEP é obrigatório");
        }

        String cepLimpo = cepBruto.replaceAll("[^0-9]", "");
        if (!cepLimpo.matches("\\d{8}")) {
            throw new BadRequestException("CEP invalido: Favor informar 8 digitos númericos");
        }

        return cepLimpo;
    }

}
