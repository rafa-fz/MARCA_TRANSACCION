package com.banquito.marca.aplicacion.controlador;

import com.banquito.marca.aplicacion.controlador.dto.peticion.TransaccionPeticionDTO;
import com.banquito.marca.aplicacion.controlador.dto.respuesta.TarjetaRespuestaDTO;
import com.banquito.marca.aplicacion.controlador.dto.respuesta.TransaccionRespuestaDTO;
import com.banquito.marca.aplicacion.controlador.mapper.ITransaccionPeticionMapper;
import com.banquito.marca.aplicacion.controlador.mapper.ITransaccionRespuestaMapper;
import com.banquito.marca.aplicacion.modelo.Tarjeta;
import com.banquito.marca.aplicacion.modelo.Transaccion;
import com.banquito.marca.aplicacion.servicio.TarjetaService;
import com.banquito.marca.aplicacion.servicio.TransaccionService;
import com.banquito.marca.aplicacion.servicio.ValidadorTarjetasService;
import com.banquito.marca.compartido.excepciones.OperacionInvalidaExcepcion;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/transacciones")
public class TransaccionController {
    private final TransaccionService transaccionService;
    private final ITransaccionPeticionMapper transaccionPeticionMapper;
    private final ITransaccionRespuestaMapper transaccionRespuestaMapper;

    private final TarjetaService tarjetaService;

    private final ValidadorTarjetasService validadorTarjetasService;

    public TransaccionController(
            TransaccionService transaccionService,
            ITransaccionPeticionMapper transaccionPeticionMapper,
            ITransaccionRespuestaMapper transaccionRespuestaMapper,
            TarjetaService tarjetaService,
            ValidadorTarjetasService validadorTarjetasService
    ) {
        this.transaccionService = transaccionService;
        this.transaccionPeticionMapper = transaccionPeticionMapper;
        this.transaccionRespuestaMapper = transaccionRespuestaMapper;
        this.tarjetaService = tarjetaService;
        this.validadorTarjetasService = validadorTarjetasService;
    }

    @PostMapping
    public ResponseEntity<?> almacenar(@Valid @RequestBody TransaccionPeticionDTO transaccionPeticionDTO) {
        if (this.validadorTarjetasService.esNumeroTarjetaValido(transaccionPeticionDTO.getNumeroTarjeta()))
            throw new OperacionInvalidaExcepcion("La tarjeta no es válida");

        Transaccion transaccion = this.transaccionPeticionMapper.toPersistence(transaccionPeticionDTO);
        Tarjeta tarjeta = this.tarjetaService.buscarPorNuemro(transaccionPeticionDTO.getNumeroTarjeta());

        this.transaccionService.registrarTransaccion(transaccion, tarjeta, transaccionPeticionDTO.getCvv(), transaccionPeticionDTO.getFechaCaducidad());
        TransaccionRespuestaDTO respuestaDTO = this.transaccionRespuestaMapper.toDto(transaccion);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuestaDTO);
    }
}
