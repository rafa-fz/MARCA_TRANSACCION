package com.banquito.marca.aplicacion.controlador.mapper;

import com.banquito.marca.aplicacion.dto.respuesta.TransaccionRespuestaDTO;
import com.banquito.marca.aplicacion.modelo.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ITransaccionRespuestaMapper {
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "fechaRecepcion", target = "fechaHora")
    @Mapping(source = "monto", target = "valor")
    TransaccionRespuestaDTO toDto(Transaccion transaccion);
    
    @Mapping(source = "fechaHora", target = "fechaRecepcion")
    @Mapping(source = "valor", target = "monto")
    Transaccion toPersistence(TransaccionRespuestaDTO dto);
}
