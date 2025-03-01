package com.banquito.marca.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaRequestDTO;
import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaResponseDTO;

@FeignClient(name = "tarjetaClient", url = "${tarjeta.service.url}")
public interface TarjetaClient {
    
    @PostMapping("/api/v1/tarjetas/validar")
    ValidacionTarjetaResponseDTO validarTarjeta(@RequestBody ValidacionTarjetaRequestDTO request);
} 