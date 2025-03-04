package com.banquito.marca.transaccion.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.marca.transaccion.client.TarjetaClient;
import com.banquito.marca.transaccion.controller.dto.ProcesadorRespuestaDTO;
import com.banquito.marca.transaccion.controller.dto.SwiftResponseDTO;
import com.banquito.marca.transaccion.controller.dto.TransaccionPeticionDTO;
import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaRequestDTO;
import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaResponseDTO;
import com.banquito.marca.transaccion.excepcion.TransaccionNotFoundException;
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

    @Transactional
    public ProcesadorRespuestaDTO validarTarjeta(TransaccionPeticionDTO peticionDTO) {
        // Preparamos la solicitud de validación
        ValidacionTarjetaRequestDTO validacionRequest = new ValidacionTarjetaRequestDTO();
        validacionRequest.setNumeroTarjeta(peticionDTO.getNumeroTarjeta());
        validacionRequest.setCvv(peticionDTO.getCvv());
        validacionRequest.setFechaCaducidad(peticionDTO.getFechaCaducidad());

        // Validamos la tarjeta
        ValidacionTarjetaResponseDTO respuesta = tarjetaClient.validarTarjeta(validacionRequest);

        // Obtenemos el código SWIFT
        SwiftResponseDTO swiftResponse = tarjetaClient.obtenerSwift(peticionDTO.getNumeroTarjeta());

        // Creamos la respuesta en el formato requerido
        ProcesadorRespuestaDTO procesadorRespuesta = new ProcesadorRespuestaDTO();
        procesadorRespuesta.setEsValida(respuesta.getEsValida());
        procesadorRespuesta.setMensaje(respuesta.getMensaje());
        procesadorRespuesta.setSwiftBanco(swiftResponse.getSwiftBanco());

        // Si la validación es exitosa, guardamos la transacción
        if (respuesta.getEsValida()) {
            Transaccion transaccion = new Transaccion();
            transaccion.setCodigoUnicoTransaccion(peticionDTO.getCodigoUnicoTransaccion());
            transaccion.setNumeroTarjeta(peticionDTO.getNumeroTarjeta());
            transaccion.setCvv(peticionDTO.getCvv());
            transaccion.setFechaCaducidad(peticionDTO.getFechaCaducidad());
            transaccion.setMonto(peticionDTO.getMonto());
            transaccion.setFechaRecepcion(LocalDateTime.now());
            transaccion.setFechaRespuesta(LocalDateTime.now());
            transaccion.setEstado(true);

            // Encriptamos los datos sensibles
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
            transaccionRepository.save(transaccion);
        }

        return procesadorRespuesta;
    }

    @Transactional(readOnly = true)
    public Transaccion buscarTransaccion(String codigoUnico) {
        return transaccionRepository.findById(codigoUnico)
                .orElseThrow(() -> new TransaccionNotFoundException("Transacción no encontrada con código: " + codigoUnico));
    }

    @Transactional(readOnly = true)
    public Page<Transaccion> listarTransacciones(Pageable pageable) {
        return transaccionRepository.findAll(pageable);
    }
}