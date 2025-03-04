package com.banquito.marca.transaccion.controller.mapper;

import org.springframework.stereotype.Component;

import com.banquito.marca.transaccion.controller.dto.TransaccionPeticionDTO;
import com.banquito.marca.transaccion.model.Transaccion;

@Component
public class TransaccionPeticionMapper {

    public Transaccion toPersistance(TransaccionPeticionDTO dto) {
        if (dto == null) {
            return null;
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setCodigoUnicoTransaccion(dto.getCodigoUnicoTransaccion());
        transaccion.setNumeroTarjeta(dto.getNumeroTarjeta());
        transaccion.setCvv(dto.getCvv());
        transaccion.setFechaCaducidad(dto.getFechaCaducidad());
        transaccion.setMonto(dto.getMonto());
        
        return transaccion;
    }

    public TransaccionPeticionDTO toDto(Transaccion entity) {
        if (entity == null) {
            return null;
        }

        TransaccionPeticionDTO dto = new TransaccionPeticionDTO();
        dto.setCodigoUnicoTransaccion(entity.getCodigoUnicoTransaccion());
        dto.setNumeroTarjeta(entity.getNumeroTarjeta());
        dto.setCvv(entity.getCvv());
        dto.setFechaCaducidad(entity.getFechaCaducidad());
        dto.setMonto(entity.getMonto());
        
        return dto;
    }
}
