package com.banquito.marca.compartido.excepciones;

public class EntidadDuplicadaExcepcion extends RuntimeException {
    public EntidadDuplicadaExcepcion(String message) {
        super(message);
    }
}