package com.banquito.marca.aplicacion.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banquito.marca.aplicacion.dto.TransaccionBancoDTO;
import com.banquito.marca.aplicacion.dto.peticion.TransaccionPeticionDTO;

@FeignClient(name = "tarjeta-service", url = "${tarjeta.service.url}")
public interface TarjetaClient {

    @PostMapping("/api/v1/tarjetas/validar")
    TransaccionBancoDTO validarTarjeta(@RequestBody TransaccionPeticionDTO transaccion);
}
