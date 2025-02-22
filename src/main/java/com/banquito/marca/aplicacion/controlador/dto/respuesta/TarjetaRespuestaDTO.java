package com.banquito.marca.aplicacion.controlador.dto.respuesta;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TarjetaRespuestaDTO {
    private String numero;
    private String fechaCaducidad;
    private String cvv;
    private String tipo;
    private String franquicia;
    private LocalDateTime fechaEmision;
}
