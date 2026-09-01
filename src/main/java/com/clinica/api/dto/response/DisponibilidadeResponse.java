package com.clinica.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DisponibilidadeResponse {

    private Long medicoId;
    private String medico;
    private LocalDate data;
    private List<HorarioResponse> horarios;

    @Getter
    @AllArgsConstructor
    public static class HorarioResponse {

        private String hora;
        private boolean disponivel;

    }

}
