package com.banquito.marca.compartido.excepciones;

public class EntidadNoEncontradaExcepcion extends RuntimeException {
    public EntidadNoEncontradaExcepcion(String message) {
        super(message);
    }
}