package com.banquito.marca.aplicacion.servicio;

import com.banquito.marca.aplicacion.modelo.Tarjeta;
import com.banquito.marca.compartido.excepciones.OperacionInvalidaExcepcion;
import com.banquito.marca.compartido.utilidades.UtilidadHash;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class ValidadorTarjetasService {
    public Boolean esNumeroTarjetaValido(String numeroTarjeta) {
        int suma = 0;
        boolean alternar = false;

        for (int i = numeroTarjeta.length() - 1; i >= 1; i--) {
            int digito = Character.getNumericValue(numeroTarjeta.charAt(i));

            if (alternar) {
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }

            suma += digito;
            alternar = !alternar;
        }

        return suma % 10 == 0;
    }

    public void esTarjetaValida(Tarjeta tarjeta, String fechaCaducidad, String cvv) {
        if (!UtilidadHash.verificarString(cvv, tarjeta.getCvv()))
            throw new OperacionInvalidaExcepcion("Código de seguridad de la tarjeta incorrecto");

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth fechaEntrada = YearMonth.parse(fechaCaducidad, inputFormatter);;
        YearMonth fechaBaseDatos = YearMonth.from(tarjeta.getFechaCaducidad());

        if (!fechaEntrada.equals(fechaBaseDatos))
            throw new OperacionInvalidaExcepcion("La fecha de caducidad de la tarjeta incorrecta");
    }
}
