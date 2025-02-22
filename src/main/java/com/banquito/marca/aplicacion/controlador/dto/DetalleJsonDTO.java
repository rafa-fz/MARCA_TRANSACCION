package com.banquito.marca.aplicacion.controlador.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleJsonDTO {
    private ItemComisionDTO gtw;
    private ItemComisionDTO processor;
    private ItemComisionDTO marca;
}
