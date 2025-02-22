package com.banquito.marca.compartido.excepciones;

import com.banquito.marca.compartido.respuestas.RespuestaApi;
import com.banquito.marca.compartido.utilidades.UtilidadRespuesta;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ManejadorExcepcionesGlobal {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaApi<?>> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                UtilidadRespuesta.error(
                        "VALIDACION_ERROR",
                        "Errores de validación en la solicitud.",
                        "Corrige los campos indicados y vuelve a intentar.",
                        errores
                )
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(
                UtilidadRespuesta.error(
                        "ERROR",
                        ex.getReason(),
                        null
                )
        );
    }

    @ExceptionHandler(EntidadNoEncontradaExcepcion.class)
    public ResponseEntity<?> handleEntidadNoEncontradaException(EntidadNoEncontradaExcepcion ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                UtilidadRespuesta.error(
                        "RECURSO_NO_ENCONTRADO",
                        ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(EntidadDuplicadaExcepcion.class)
    public ResponseEntity<?> handleEntidadDuplicadaException(EntidadDuplicadaExcepcion ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                UtilidadRespuesta.error(
                        "RECURSO_DUPLICADO",
                        ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(OperacionInvalidaExcepcion.class)
    public ResponseEntity<?> handleOperacionInvalidaException(OperacionInvalidaExcepcion ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                UtilidadRespuesta.error(
                        "OPERACION_INVALIDA",
                        ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                UtilidadRespuesta.error(
                        "ERROR_INTERNO",
                        "Error de acceso a datos.",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                UtilidadRespuesta.error(
                        "ERROR_INTERNO",
                        "Violación de restricciones.",
                        ex.getMessage()
                )
        );
    }
}