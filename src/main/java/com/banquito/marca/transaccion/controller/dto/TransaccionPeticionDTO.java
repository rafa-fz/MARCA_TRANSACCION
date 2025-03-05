package com.banquito.marca.transaccion.controller.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la petición de creación de transacción")
public class TransaccionPeticionDTO {

    @Schema(description = "Código único de la transacción", example = "TRX1234567891011")
    private String codigoUnicoTransaccion;

    @Schema(description = "Número de tarjeta", example = "5135860771089499")
    private String numeroTarjeta;

    @Schema(description = "CVV de la tarjeta", example = "553")
    private String cvv;

    @Schema(description = "Fecha de caducidad de la tarjeta (MM/YY)", example = "03/29")
    private String fechaCaducidad;

    @Schema(description = "Monto de la transacción", example = "100.50")
    private BigDecimal monto;

    public TransaccionPeticionDTO() {
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
        TransaccionPeticionDTO other = (TransaccionPeticionDTO) obj;
        if (codigoUnicoTransaccion == null) {
            if (other.codigoUnicoTransaccion != null)
                return false;
        } else if (!codigoUnicoTransaccion.equals(other.codigoUnicoTransaccion))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TransaccionPeticionDTO [codigoUnicoTransaccion=" + codigoUnicoTransaccion + ", numeroTarjeta="
                + numeroTarjeta + ", cvv=" + cvv + ", fechaCaducidad=" + fechaCaducidad + ", monto=" + monto + "]";
    }

}
