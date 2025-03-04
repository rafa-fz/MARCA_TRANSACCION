package com.banquito.marca.transaccion.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la respuesta del procesador de tarjetas")
public class ProcesadorRespuestaDTO {

    @Schema(description = "Indica si la tarjeta es válida", example = "true")
    private boolean esValida;

    @Schema(description = "Mensaje descriptivo del resultado", example = "La tarjeta es válida")
    private String mensaje;

    @Schema(description = "Código SWIFT del banco", example = "PICHECU0001")
    private String swiftBanco;

    public ProcesadorRespuestaDTO() {
    }

    public boolean isEsValida() {
        return esValida;
    }

    public void setEsValida(boolean esValida) {
        this.esValida = esValida;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSwiftBanco() {
        return swiftBanco;
    }

    public void setSwiftBanco(String swiftBanco) {
        this.swiftBanco = swiftBanco;
    }
} 