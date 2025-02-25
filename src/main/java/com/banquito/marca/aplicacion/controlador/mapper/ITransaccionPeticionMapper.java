package com.banquito.marca.aplicacion.controlador.mapper;

import com.banquito.marca.aplicacion.dto.peticion.TransaccionPeticionDTO;
import com.banquito.marca.aplicacion.modelo.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ITransaccionPeticionMapper {
    TransaccionPeticionDTO toDto(Transaccion transaccion);
    Transaccion toPersistence(TransaccionPeticionDTO dto);
}
