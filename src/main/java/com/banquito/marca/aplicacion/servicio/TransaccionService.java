package com.banquito.marca.aplicacion.servicio;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.banquito.marca.aplicacion.modelo.Transaccion;
import com.banquito.marca.aplicacion.repositorio.TransaccionRepository;
import com.banquito.marca.aplicacion.utilidad.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final EncryptionUtil encryptionUtil;
    private final ObjectMapper objectMapper;

    public TransaccionService(
            TransaccionRepository transaccionRepository,
            EncryptionUtil encryptionUtil) {
        this.transaccionRepository = transaccionRepository;
        this.encryptionUtil = encryptionUtil;
        this.objectMapper = new ObjectMapper();
    }

    public Transaccion registrarTransaccion(Transaccion transaccion) {
        try {
            log.info("Iniciando registro de transacción: {}", transaccion);

            // Setear fechas primero
            setearFechas(transaccion);
            log.info("Fechas seteadas: {}", transaccion);

            // Validar datos requeridos
            validarDatosRequeridos(transaccion);
            log.info("Datos validados correctamente");

            // Encriptar datos sensibles
            encriptarDatos(transaccion);
            log.info("Datos encriptados correctamente");

            // Guardar transacción
            transaccion.setEstado(true);
            Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
            log.info("Transacción guardada exitosamente: {}", transaccionGuardada);

            return transaccionGuardada;

        } catch (Exception e) {
            log.error("Error detallado al registrar la transacción: ", e);
            throw new RuntimeException("Error al registrar la transacción: " + e.getMessage(), e);
        }
    }

    private void encriptarDatos(Transaccion transaccion) {
        try {
            Map<String, Object> datosEncriptar = new HashMap<>();
            datosEncriptar.put("numeroTarjeta", transaccion.getNumeroTarjeta());
            datosEncriptar.put("cvv", transaccion.getCvv());
            datosEncriptar.put("fechaCaducidad", transaccion.getFechaCaducidad());
            datosEncriptar.put("monto", transaccion.getMonto());

            String jsonDatos = objectMapper.writeValueAsString(datosEncriptar);
            String datosEncriptados = encryptionUtil.encrypt(jsonDatos);

            transaccion.setTransaccionEncriptada(datosEncriptados);

        } catch (Exception e) {
            log.error("Error al encriptar datos: {}", e.getMessage());
            throw new RuntimeException("Error al encriptar datos de la transacción", e);
        }
    }

    private void validarDatosRequeridos(Transaccion transaccion) {
        log.info("Validando transacción: {}", transaccion);

        if (transaccion == null) {
            throw new IllegalArgumentException("La transacción no puede ser nula");
        }

        StringBuilder errores = new StringBuilder();
        
        if (transaccion.getNumeroTarjeta() == null || transaccion.getNumeroTarjeta().trim().isEmpty()) {
            errores.append("Número de tarjeta es requerido. ");
        }
        if (transaccion.getCvv() == null || transaccion.getCvv().trim().isEmpty()) {
            errores.append("CVV es requerido. ");
        }
        if (transaccion.getMonto() == null) {
            errores.append("Monto es requerido. ");
        }
        if (transaccion.getFechaCaducidad() == null) {
            errores.append("Fecha de caducidad es requerida. ");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }

    private void setearFechas(Transaccion transaccion) {
        LocalDateTime fecha = LocalDateTime.now();
        transaccion.setFechaRecepcion(fecha);
        transaccion.setFechaRespuesta(fecha);
    }

    public Page<Transaccion> listarTransacciones(
            String estado,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            String numeroTarjeta,
            Pageable pageable) {
        return transaccionRepository.findAll(pageable);
    }
}