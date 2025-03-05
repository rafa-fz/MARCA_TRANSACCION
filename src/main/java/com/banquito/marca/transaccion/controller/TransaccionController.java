package com.banquito.marca.transaccion.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.marca.transaccion.controller.dto.ProcesadorRespuestaDTO;
import com.banquito.marca.transaccion.controller.dto.TransaccionPeticionDTO;
import com.banquito.marca.transaccion.model.Transaccion;
import com.banquito.marca.transaccion.service.TransaccionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/procesador/tarjetas")
@Tag(name = "Procesador de Tarjetas", description = "API para procesar y validar tarjetas")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @Operation(summary = "Validar una tarjeta", description = "Valida una tarjeta y retorna su estado junto con el código SWIFT del banco")
    @PostMapping("/validar")
    public ResponseEntity<ProcesadorRespuestaDTO> validarTarjeta(@RequestBody TransaccionPeticionDTO peticionDTO) {
        return ResponseEntity.ok(transaccionService.validarTarjeta(peticionDTO));
    }

    @Operation(summary = "Obtener transacción por código", description = "Obtiene los detalles de una transacción específica")
    @GetMapping("/transacciones/{codigoUnico}")
    public ResponseEntity<Transaccion> obtenerTransaccion(@PathVariable("codigoUnico") String codigoUnico) {
        return ResponseEntity.ok(transaccionService.buscarTransaccion(codigoUnico));
    }

    @Operation(summary = "Listar transacciones", description = "Obtiene una lista paginada de transacciones")
    @GetMapping("/transacciones")
    public ResponseEntity<Page<Transaccion>> listarTransacciones(Pageable pageable) {
        return ResponseEntity.ok(transaccionService.listarTransacciones(pageable));
    }
}
