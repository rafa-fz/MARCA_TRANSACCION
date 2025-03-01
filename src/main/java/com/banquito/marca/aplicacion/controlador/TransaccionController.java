package com.banquito.marca.aplicacion.controlador;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.marca.aplicacion.controlador.mapper.ITransaccionPeticionMapper;
import com.banquito.marca.aplicacion.controlador.mapper.ITransaccionRespuestaMapper;
import com.banquito.marca.aplicacion.dto.peticion.TransaccionPeticionDTO;
import com.banquito.marca.aplicacion.dto.respuesta.TransaccionRespuestaDTO;
import com.banquito.marca.aplicacion.modelo.Transaccion;
import com.banquito.marca.aplicacion.servicio.TransaccionService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/transacciones")
@CrossOrigin("*")
@Tag(name = "Transacciones", description = "Documentacion Transacciones")
@Slf4j
public class TransaccionController {
        private final TransaccionService transaccionService;
        private final ITransaccionPeticionMapper transaccionPeticionMapper;
        private final ITransaccionRespuestaMapper transaccionRespuestaMapper;
        private final Cache<String, Page<TransaccionRespuestaDTO>> responseCache;

        public TransaccionController(
                        TransaccionService transaccionService,
                        ITransaccionPeticionMapper transaccionPeticionMapper,
                        ITransaccionRespuestaMapper transaccionRespuestaMapper) {
                this.transaccionService = transaccionService;
                this.transaccionPeticionMapper = transaccionPeticionMapper;
                this.transaccionRespuestaMapper = transaccionRespuestaMapper;
                this.responseCache = Caffeine.newBuilder()
                                .expireAfterWrite(24, TimeUnit.HOURS)
                                .maximumSize(10_000)
                                .build();
        }

        @Operation(summary = "Registrar una transacción", description = "Registra una nueva transacción validando el número de tarjeta y su información asociada.", requestBody = @RequestBody(description = "Información necesaria para registrar una transacción", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransaccionPeticionDTO.class))))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Transacción registrada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransaccionRespuestaDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o tarjeta no válida", content = @Content(mediaType = "application/json")),

                        @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransaccionRespuestaDTO.class)))
        })

        @PostMapping
        public ResponseEntity<?> almacenar(@Valid @RequestBody TransaccionPeticionDTO transaccionPeticionDTO) {
                try {
                        log.info("Recibiendo petición de transacción: {}", transaccionPeticionDTO);
                        
                        Transaccion transaccion = this.transaccionPeticionMapper.toPersistence(transaccionPeticionDTO);
                        log.info("Transacción mapeada: {}", transaccion);
                        
                        transaccion.setEstado(true);

                        // Guardar la transacción
                        Transaccion transaccionGuardada = this.transaccionService.registrarTransaccion(transaccion);
                        log.info("Transacción guardada: {}", transaccionGuardada);
                        
                        TransaccionRespuestaDTO respuestaDTO = this.transaccionRespuestaMapper.toDto(transaccionGuardada);
                        return ResponseEntity.status(HttpStatus.CREATED).body(respuestaDTO);

                } catch (Exception e) {
                        log.error("Error completo al procesar la transacción: ", e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Error al procesar la transacción: " + e.getMessage() + 
                                              (e.getCause() != null ? ". Causa: " + e.getCause().getMessage() : ""));
                }
        }

        @GetMapping
        public ResponseEntity<Page<TransaccionRespuestaDTO>> listar(
                        @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
                        @PageableDefault(size = 20, sort = "fechaHora", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false) String estado,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
                        @RequestParam(required = false) String numeroTarjeta) {
                if (idempotencyKey == null || idempotencyKey.isEmpty())
                        idempotencyKey = UUID.randomUUID().toString();

                Page<TransaccionRespuestaDTO> cachedResponse = responseCache.getIfPresent(idempotencyKey);
                if (cachedResponse != null)
                        return ResponseEntity.ok(cachedResponse);

                Page<Transaccion> transacciones = transaccionService.listarTransacciones(
                                estado, fechaDesde, fechaHasta, numeroTarjeta, pageable);
                Page<TransaccionRespuestaDTO> respuesta = transacciones.map(transaccionRespuestaMapper::toDto);
                responseCache.put(idempotencyKey, respuesta);

                return ResponseEntity.ok(respuesta);
        }
}
