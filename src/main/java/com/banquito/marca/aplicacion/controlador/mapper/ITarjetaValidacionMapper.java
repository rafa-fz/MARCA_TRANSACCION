package com.banquito.marca.aplicacion.controlador.mapper;

import com.banquito.marca.aplicacion.controlador.dto.peticion.TarjetaValidacionDTO;
import com.banquito.marca.aplicacion.modelo.Tarjeta;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ITarjetaValidacionMapper {
    TarjetaValidacionDTO toDto(Tarjeta tarjeta);
    Tarjeta toPersistence(TarjetaValidacionDTO tarjetaDTO);
}
