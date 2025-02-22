package com.banquito.marca.aplicacion.controlador.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ItemComisionDTO {
    private String referencia;
    private BigDecimal comision;
    private String numeroCuenta;
}
