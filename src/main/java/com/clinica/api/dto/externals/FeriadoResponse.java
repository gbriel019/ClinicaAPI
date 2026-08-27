package com.clinica.api.dto.externals;


import lombok.Data;

import java.time.LocalDate;

@Data
public class FeriadoResponse {

    private LocalDate date;
    private String name;
    private String type;

}
