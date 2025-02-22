package com.banquito.marca.aplicacion.controlador.dto.peticion;

import com.banquito.marca.aplicacion.controlador.dto.DetalleJsonDTO;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransaccionPeticionDTO {
    @NotNull(message = "El numero de la tarjeta no debe ser NULO")
    @NotEmpty(message = "EL numero no debe estar VACIO")
    @NotBlank(message = "El número de tarjeta no debe ser vacío")
    @Size(max = 16, message = "El número de tarjeta debe tener un máximo de 16 caracteres")
    private String numeroTarjeta;
    
    @NotBlank(message = "El CVV no debe ser vacío ni nulo")
    @Size(max = 3, message = "El CVV debe tener un máximo de 3 caracteres")
    private String cvv;
    
    @NotEmpty(message = "La fecha de caducidad no debe ser vacía ni nula")
    @Size(max = 5, message = "La fecha de caducidad debe tener un maximo de 5 caracteres")
    private String fechaCaducidad;
    
    @NotEmpty(message = "El valor no debe ser vacío ni nulo")
    @Positive(message = "El valor debe ser positivo")
    private BigDecimal valor;
    
    @NotBlank(message = "La descripción no debe ser vacía ni nula")
    @Size(max = 100, message = "La descripción debe tener un máximo de 100 caracteres")
    private String descripcion;
    
    @NotBlank(message = "El beneficiario no debe ser vacío ni nulo")
    @Size(max = 100, message = "El beneficiario debe tener un máximo de 100 caracteres")
    private String beneficiario;
    
    @NotBlank(message = "El número de cuenta no debe ser vacío ni nulo")
    @Size(max = 15, message = "El número de cuenta no debe tener entre 8 y 15 caracteres")
    private String numeroCuenta;
    
    @NotEmpty(message = "El campo esDiferido no debe ser vacío ni nulo")
    private Boolean esDiferido;
    
    @NotEmpty(message = "El número de cuotas no debe ser vacío ni nulo")
    @Positive(message = "El valor debe ser positivo")
    private Integer cuotas;
    
    @NotEmpty(message = "El detalle no debe ser vacío ni nulo")
    private DetalleJsonDTO detalle;
}
