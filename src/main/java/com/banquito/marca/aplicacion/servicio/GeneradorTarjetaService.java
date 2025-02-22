package com.banquito.marca.aplicacion.servicio;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GeneradorTarjetaService {
    public String generarNumeroTarjetaVisaValido() {
        Random aleatorio = new Random();
        StringBuilder numeroTarjeta = new StringBuilder("4");

        for (int i = 1; i < 15; i++)
            numeroTarjeta.append(aleatorio.nextInt(10));

        int digitoControl = calcularDigitoControlLuhn(numeroTarjeta.toString());
        numeroTarjeta.append(digitoControl);

        return numeroTarjeta.toString();
    }

    public String generarNumeroTarjetaMasterCardValido() {
        Random aleatorio = new Random();
        StringBuilder numeroTarjeta = new StringBuilder("5");

        for (int i = 1; i < 15; i++)
            numeroTarjeta.append(aleatorio.nextInt(10));

        int digitoControl = calcularDigitoControlLuhn(numeroTarjeta.toString());
        numeroTarjeta.append(digitoControl);

        return numeroTarjeta.toString();
    }

    public String generarCVV() {
        Random aleatorio = new Random();
        int cvv = 100 + aleatorio.nextInt(900);
        return String.valueOf(cvv);
    }

    private int calcularDigitoControlLuhn(String numeroTarjeta) {
        int suma = 0;
        boolean alternar = true;

        for (int i = numeroTarjeta.length() - 1; i >= 0; i--) {
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

        return (10 - (suma % 10)) % 10;
    }
}
