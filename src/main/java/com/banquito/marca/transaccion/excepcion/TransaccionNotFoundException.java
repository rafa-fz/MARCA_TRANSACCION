package com.banquito.marca.transaccion.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransaccionNotFoundException extends RuntimeException {
    
    public TransaccionNotFoundException(String message) {
        super(message);
    }

    public TransaccionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 