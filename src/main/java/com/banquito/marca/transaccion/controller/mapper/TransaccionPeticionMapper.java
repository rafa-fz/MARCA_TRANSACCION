package com.banquito.marca.transaccion.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.banquito.marca.transaccion.controller.dto.TransaccionPeticionDTO;
import com.banquito.marca.transaccion.model.Transaccion;

@Mapper(componentModel = "spring")
public interface TransaccionPeticionMapper {
    
    @Mapping(target = "fechaRecepcion", ignore = true)
    @Mapping(target = "fechaRespuesta", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "transaccionEncriptada", ignore = true)
    Transaccion toPersistance(TransaccionPeticionDTO dto);

    TransaccionPeticionDTO toDTO(Transaccion transaccion);
}
