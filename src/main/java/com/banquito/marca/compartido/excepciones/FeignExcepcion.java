package com.banquito.marca.compartido.excepciones;

public class FeignExcepcion extends RuntimeException {

    private final Integer status;
    private final String errorBody;

    public FeignExcepcion(int status, String errorBody) {
        super("Error en el cliente Feign: status=" + status + ", body=" + errorBody);
        this.status = status;
        this.errorBody = errorBody;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorBody() {
        return errorBody;
    }
}