package com.banquito.marca.aplicacion.http;

import com.banquito.marca.aplicacion.dto.TransaccionBancoDTO;
import com.banquito.marca.aplicacion.dto.peticion.TransaccionPeticionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(name = "CBS-API", url = "${external.cbs.api.base-url}")
public interface CbsClient {
    @RequestMapping(method = RequestMethod.POST, value = "/v1/transacciones/consumo-tarjeta")
    TransaccionBancoDTO enviarTransaccionBanco(TransaccionPeticionDTO transaccion);
}
