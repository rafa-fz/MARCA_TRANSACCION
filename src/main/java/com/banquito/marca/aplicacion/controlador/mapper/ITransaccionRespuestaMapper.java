package com.banquito.marca.aplicacion.controlador.mapper;

import com.banquito.marca.aplicacion.controlador.dto.respuesta.TransaccionRespuestaDTO;
import com.banquito.marca.aplicacion.modelo.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ITransaccionRespuestaMapper {
    TransaccionRespuestaDTO toDto(Transaccion transaccion);
    Transaccion toPersistence(TransaccionRespuestaDTO dto);
}
