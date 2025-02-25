package com.banquito.marca.aplicacion.dto.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(name = "Respuesta Tarjeta")
public class TarjetaRespuestaDTO {

    @Schema(description = "Número de la tarjeta",
            example = "1234 5678 9876 5432",
            required = true)
    private String numero;

    @Schema(description = "Fecha de caducidad de la tarjeta (formato: MM/AA)",
            example = "12/24")
    private String fechaCaducidad;

    @Schema(description = "Código de seguridad de la tarjeta (CVV)",
            example = "123")
    private String cvv;

    @Schema(description = "Tipo de tarjeta (por ejemplo, crédito o débito)",
            example = "Crédito")
    private String tipo;

    @Schema(description = "Franquicia de la tarjeta (por ejemplo, Visa, MasterCard)",
            example = "Visa")
    private String franquicia;

    @Schema(description = "Fecha de emisión de la tarjeta",
            example = "2023-10-01T15:30:00")
    private LocalDateTime fechaEmision;
}
