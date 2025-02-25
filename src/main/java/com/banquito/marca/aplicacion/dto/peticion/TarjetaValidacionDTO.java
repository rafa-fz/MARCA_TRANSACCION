package com.banquito.marca.aplicacion.dto.peticion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TarjetaValidacionDTO {
    @NotBlank(message = "El número de tarjeta no debe ser vacío ni nulo")
    @Size(max = 16, message = "El número de tarjeta debe tener un máximo de 16 caracteres")
    String numero;
    @NotEmpty(message = "La fecha de caducidad no debe ser vacía ni nula")
    @Size(max = 5, message = "La fecha de caducidad debe tener un maximo de 5 caracteres")
    String fechaCaducidad;
    @NotBlank(message = "El CVV no debe ser vacío ni nulo")
    @Size(max = 3, message = "El CVV debe tener un máximo de 3 caracteres")
    String cvv;
}
