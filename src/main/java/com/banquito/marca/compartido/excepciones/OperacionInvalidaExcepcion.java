package com.banquito.marca.compartido.excepciones;

public class OperacionInvalidaExcepcion extends RuntimeException {
    public OperacionInvalidaExcepcion(String message) {
        super(message);
    }
}