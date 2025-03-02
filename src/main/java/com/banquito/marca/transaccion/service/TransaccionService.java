package com.banquito.marca.transaccion.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banquito.marca.transaccion.client.TarjetaClient;
import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaRequestDTO;
import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaResponseDTO;
import com.banquito.marca.transaccion.model.Transaccion;
import com.banquito.marca.transaccion.repository.TransaccionRepository;
import com.banquito.marca.transaccion.utils.EncriptacionUtil;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TarjetaClient tarjetaClient;
    private final EncriptacionUtil encriptacionUtil;

    @Autowired
    public TransaccionService(TransaccionRepository transaccionRepository, 
                            TarjetaClient tarjetaClient,
                            EncriptacionUtil encriptacionUtil) {
        this.transaccionRepository = transaccionRepository;
        this.tarjetaClient = tarjetaClient;
        this.encriptacionUtil = encriptacionUtil;
    }

    public Transaccion crearTransaccion(Transaccion transaccion) {
        // Establecemos la fecha de recepción al inicio
        transaccion.setFechaRecepcion(LocalDateTime.now());

        // Preparamos la solicitud de validación
        ValidacionTarjetaRequestDTO validacionRequest = new ValidacionTarjetaRequestDTO();
        validacionRequest.setNumeroTarjeta(transaccion.getNumeroTarjeta());
        validacionRequest.setCvv(transaccion.getCvv());
        validacionRequest.setFechaCaducidad(transaccion.getFechaCaducidad());

        // Validamos la tarjeta
        ValidacionTarjetaResponseDTO respuesta = tarjetaClient.validarTarjeta(validacionRequest);
        
        // Establecemos la fecha de respuesta cuando recibimos la validación
        transaccion.setFechaRespuesta(LocalDateTime.now());
        transaccion.setEstado(respuesta.getEsValida());
        
        // Encriptamos los datos sensibles de la transacción
        String datosParaEncriptar = String.format(
            "CodigoUnicoTransaccion=%s;numeroTarjeta=%s;cvv=%s;fechaCaducidad=%s;FechaRecepcion=%s;FechaRespuesta=%s;monto=%s;estado=%s",
            transaccion.getCodigoUnicoTransaccion(),
            transaccion.getNumeroTarjeta(),
            transaccion.getCvv(),
            transaccion.getFechaCaducidad(),
            transaccion.getFechaRecepcion(),
            transaccion.getFechaRespuesta(),
            transaccion.getMonto(),
            transaccion.isEstado()
        );
        
        transaccion.setTransaccionEncriptada(encriptacionUtil.encriptar3DES(datosParaEncriptar));
        
        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
        
        if (!respuesta.getEsValida()) {
            throw new RuntimeException("Tarjeta inválida");
        }

        return transaccionGuardada;
    }

    public Transaccion buscarPorId(String codigoUnicoTransaccion) {
        return transaccionRepository.findById(codigoUnicoTransaccion)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
    }

    public Transaccion procesarTransaccion(String codigoUnicoTransaccion) {
        Transaccion transaccion = buscarPorId(codigoUnicoTransaccion);
        // Ya no establecemos fechaRespuesta aquí porque ya se estableció en la validación
        transaccion.setEstado(true); // Actualizar estado a procesado
        return transaccionRepository.save(transaccion);
    }
} 