package com.banquito.marca.transaccion.controller.dto;

public class ValidacionTarjetaResponseDTO {
    private boolean esValida;
    private String mensaje;

    public boolean getEsValida() {
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
}