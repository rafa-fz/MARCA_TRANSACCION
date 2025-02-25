package com.banquito.marca.aplicacion.dto.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(name = "Respuesta Transacciones")
public class TransaccionRespuestaDTO {

    @Schema(description = "Estado de la transacción (por ejemplo, 'Exitosa', 'Fallida')",
            example = "Exitosa",
            required = true)
    private String estado;

    @Schema(description = "Fecha y hora en que se realizó la transacción",
            example = "2023-10-01T16:30:00",
            required = true)
    private LocalDateTime fechaHora;

    @Schema(description = "Valor de la transacción",
            example = "150.75",
            required = true)
    private BigDecimal valor;

    @Schema(description = "Monto de la comisión aplicada a la transacción",
            example = "10.00")
    private BigDecimal comision;
}
