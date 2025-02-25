package com.banquito.marca.aplicacion.excepcion;

public class TransaccionRechazadaExcepcion extends RuntimeException {
    private String detalle;

    public TransaccionRechazadaExcepcion(String mensaje) {
        super(mensaje);
    }

    public TransaccionRechazadaExcepcion(String mensaje, String detalle) {
        super(mensaje);
        this.detalle = detalle;
    }

    public String getDetalle() {
        return detalle;
    }
}
