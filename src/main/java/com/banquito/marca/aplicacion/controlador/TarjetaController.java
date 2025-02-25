package com.banquito.marca.aplicacion.controlador;

import com.banquito.marca.aplicacion.dto.peticion.TarjetaPeticionDTO;
import com.banquito.marca.aplicacion.dto.peticion.TarjetaValidacionDTO;
import com.banquito.marca.aplicacion.dto.respuesta.TarjetaRespuestaDTO;
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
import com.banquito.marca.compartido.excepciones.EntidadNoEncontradaExcepcion;
import com.banquito.marca.compartido.excepciones.OperacionInvalidaExcepcion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/v1/tarjetas")
@CrossOrigin("*")
@Tag(name = "Tarjetas", description = "Documentacion Tarjetas")
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

    @Operation(
            summary = "Buscar tarjeta por número",
            description = "Devuelve una tarjeta específica buscando por su número."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarjeta encontrada exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Número de tarjeta inválido", content = @Content)
    })
    @GetMapping("/{numero}")
    public ResponseEntity<Tarjeta> obtenerTarjetaPorNumero(
            @Parameter(description = "Número de la tarjeta a buscar", required = true, example = "1234567890123456")
            @PathVariable String numero) {
        try {
            Tarjeta tarjeta = tarjetaService.buscarPorNuemro(numero);
            return ResponseEntity.ok(tarjeta);
        } catch (EntidadNoEncontradaExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(
            summary = "Crear una nueva tarjeta",
            description = "Crea una nueva tarjeta asociada a un cliente. Si el cliente no existe, se registra automáticamente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear una tarjeta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TarjetaPeticionDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarjeta creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TarjetaRespuestaDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(mediaType = "application/json")),
            
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TarjetaRespuestaDTO.class)))
    })

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
