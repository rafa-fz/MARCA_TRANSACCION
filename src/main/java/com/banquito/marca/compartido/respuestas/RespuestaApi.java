package com.banquito.marca.compartido.respuestas;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaApi<T> {
    private T datos;
    private Meta meta;
    private DetallesError error;

    public RespuestaApi() {}

    public RespuestaApi(T datos, Meta meta) {
        this.datos = datos;
        this.meta = meta;
    }

    public RespuestaApi(DetallesError error, Meta meta) {
        this.error = error;
        this.meta = meta;
    }

    public T getDatos() {
        return datos;
    }

    public void setDatos(T datos) {
        this.datos = datos;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public DetallesError getError() {
        return error;
    }

    public void setError(DetallesError error) {
        this.error = error;
    }

    public static class Meta {
        private String version;
        private String servidor;
        private String documentacion;

        public Meta(String version, String servidor, String documentacion) {
            this.version = version;
            this.servidor = servidor;
            this.documentacion = documentacion;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getServidor() {
            return servidor;
        }

        public void setServidor(String servidor) {
            this.servidor = servidor;
        }

        public String getDocumentacion() {
            return documentacion;
        }

        public void setDocumentacion(String documentacion) {
            this.documentacion = documentacion;
        }
    }

    public static class DetallesError {
        private String codigo;
        private String mensaje;
        private String detalles;
        private Map<String, String> errores;

        public DetallesError(String codigo, String mensaje, String detalles, Map<String, String> errores) {
            this.codigo = codigo;
            this.mensaje = mensaje;
            this.detalles = detalles;
            this.errores = errores;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getDetalles() {
            return detalles;
        }

        public void setDetalles(String detalles) {
            this.detalles = detalles;
        }

        public Map<String, String> getErrores() {
            return errores;
        }

        public void setErrores(Map<String, String> errores) {
            this.errores = errores;
        }
    }
}
