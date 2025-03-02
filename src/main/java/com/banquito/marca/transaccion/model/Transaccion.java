package com.banquito.marca.transaccion.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String codigoUnicoTransaccion;
    private String numeroTarjeta;
    private String cvv;
    private String fechaCaducidad;
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaRespuesta;
    private boolean estado;
    private BigDecimal monto;
    private String transaccionEncriptada;

    public Transaccion() {
    }

    public String getCodigoUnicoTransaccion() {
        return codigoUnicoTransaccion;
    }

    public void setCodigoUnicoTransaccion(String codigoUnicoTransaccion) {
        this.codigoUnicoTransaccion = codigoUnicoTransaccion;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getTransaccionEncriptada() {
        return transaccionEncriptada;
    }

    public void setTransaccionEncriptada(String transaccionEncriptada) {
        this.transaccionEncriptada = transaccionEncriptada;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigoUnicoTransaccion == null) ? 0 : codigoUnicoTransaccion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaccion other = (Transaccion) obj;
        if (codigoUnicoTransaccion == null) {
            if (other.codigoUnicoTransaccion != null)
                return false;
        } else if (!codigoUnicoTransaccion.equals(other.codigoUnicoTransaccion))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Transaccion [codigoUnicoTransaccion=" + codigoUnicoTransaccion + ", numeroTarjeta=" + numeroTarjeta
                + ", cvv=" + cvv + ", fechaCaducidad=" + fechaCaducidad + ", fechaRecepcion=" + fechaRecepcion
                + ", fechaRespuesta=" + fechaRespuesta + ", estado=" + estado + ", monto=" + monto
                + ", transaccionEncriptada=" + transaccionEncriptada + "]";
    }
}
