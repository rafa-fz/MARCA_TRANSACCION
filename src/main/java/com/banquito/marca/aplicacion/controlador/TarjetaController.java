package com.banquito.marca.aplicacion.controlador;

import com.banquito.marca.aplicacion.controlador.dto.peticion.TarjetaPeticionDTO;
import com.banquito.marca.aplicacion.controlador.dto.peticion.TarjetaValidacionDTO;
import com.banquito.marca.aplicacion.controlador.dto.respuesta.TarjetaRespuestaDTO;
import com.banquito.marca.aplicacion.controlador.mapper.IClienteMapper;
import com.banquito.marca.aplicacion.controlador.mapper.ITarjetaPeticionMapper;
import com.banquito.marca.aplicacion.controlador.mapper.ITarjetaRespuestaMapper;
import com.banquito.marca.aplicacion.controlador.mapper.ITarjetaValidacionMapper;
import com.banquito.marca.aplicacion.modelo.Cliente;
import com.banquito.marca.aplicacion.modelo.Tarjeta;
import com.banquito.marca.aplicacion.servicio.ClienteService;
import com.banquito.marca.aplicacion.servicio.GeneradorTarjetaService;
import com.banquito.marca.aplicacion.servicio.TarjetaService;
import com.banquito.marca.aplicacion.servicio.ValidadorTarjetasService;
import com.banquito.marca.compartido.excepciones.OperacionInvalidaExcepcion;
import com.banquito.marca.compartido.utilidades.UtilidadHash;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/tarjetas")
public class TarjetaController {
    private final TarjetaService tarjetaService;
    private final ValidadorTarjetasService validadorTarjetasService;

    private final ITarjetaPeticionMapper tarjetaPeticionMapper;
    private final ITarjetaRespuestaMapper tarjetaRespuestaMapper;
    private final ITarjetaValidacionMapper tarjetaValidacionMapper;

    private final ClienteService clienteService;
    private final IClienteMapper clienteMapper;

    private final GeneradorTarjetaService generadorTarjetaService;

    public TarjetaController(
            TarjetaService tarjetaService,
            ValidadorTarjetasService validadorTarjetasService,
            ITarjetaPeticionMapper tarjetaPeticionMapper,
            ITarjetaRespuestaMapper tarjetaRespuestaMapper,
            ITarjetaValidacionMapper tarjetaValidacionMapper,
            ClienteService clienteService,
            IClienteMapper clienteMapper,
            GeneradorTarjetaService generadorTarjetaService
    ) {
        this.tarjetaService = tarjetaService;
        this.validadorTarjetasService = validadorTarjetasService;

        this.tarjetaPeticionMapper = tarjetaPeticionMapper;
        this.tarjetaRespuestaMapper = tarjetaRespuestaMapper;
        this.tarjetaValidacionMapper = tarjetaValidacionMapper;

        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;

        this.generadorTarjetaService = generadorTarjetaService;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TarjetaPeticionDTO tarjetaPeticionDTO) {
        Cliente cliente = this.clienteService.buscarPorIdentificacion(tarjetaPeticionDTO.getIdentificacion());
        if (cliente == null) {
             cliente = this.clienteMapper.toPersistence(tarjetaPeticionDTO);
             this.clienteService.registrarCliente(cliente);
        }

        String cvv = this.generadorTarjetaService.generarCVV();

        Tarjeta tarjeta = this.tarjetaPeticionMapper.toPersistence(tarjetaPeticionDTO);
        tarjeta.setCvv(cvv);
        this.tarjetaService.crearTarjeta(tarjeta, cliente, tarjetaPeticionDTO.getFranquicia());

        TarjetaRespuestaDTO respuestaDTO = this.tarjetaRespuestaMapper.toDto(tarjeta);
        respuestaDTO.setCvv(cvv);
        respuestaDTO.setFranquicia(tarjetaPeticionDTO.getFranquicia());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        String fechaFormateada = tarjeta.getFechaCaducidad().format(formatter);

        respuestaDTO.setFechaCaducidad(fechaFormateada);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuestaDTO);
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validar(@Valid @RequestBody TarjetaValidacionDTO tarjetaValidacionDTO) {
        if (this.validadorTarjetasService.esNumeroTarjetaValido(tarjetaValidacionDTO.getNumero()))
            throw new OperacionInvalidaExcepcion("La tarjeta no es válida");

        Tarjeta tarjeta = this.tarjetaService.buscarPorNuemro(tarjetaValidacionDTO.getNumero());
        this.validadorTarjetasService.esTarjetaValida(tarjeta, tarjetaValidacionDTO.getFechaCaducidad(), tarjetaValidacionDTO.getCvv());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
