package com.banquito.marca.transaccion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.marca.transaccion.controller.dto.TransaccionPeticionDTO;
import com.banquito.marca.transaccion.controller.mapper.TransaccionPeticionMapper;
import com.banquito.marca.transaccion.model.Transaccion;
import com.banquito.marca.transaccion.service.TransaccionService;

@RestController
@RequestMapping("/v1/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final TransaccionPeticionMapper transaccionMapper;

    @Autowired
    public TransaccionController(TransaccionService transaccionService, 
                               TransaccionPeticionMapper transaccionMapper) {
        this.transaccionService = transaccionService;
        this.transaccionMapper = transaccionMapper;
    }

    @PostMapping
    public ResponseEntity<Transaccion> crearTransaccion(@RequestBody TransaccionPeticionDTO peticionDTO) {
        Transaccion transaccion = transaccionMapper.toPersistance(peticionDTO);
        return ResponseEntity.ok(transaccionService.crearTransaccion(transaccion));
    }

    @GetMapping("/{codigoUnicoTransaccion}")
    public ResponseEntity<Transaccion> obtenerTransaccion(@PathVariable String codigoUnicoTransaccion) {
        return ResponseEntity.ok(transaccionService.buscarPorId(codigoUnicoTransaccion));
    }

    @PutMapping("/{codigoUnicoTransaccion}/procesar")
    public ResponseEntity<Transaccion> procesarTransaccion(@PathVariable String codigoUnicoTransaccion) {
        return ResponseEntity.ok(transaccionService.procesarTransaccion(codigoUnicoTransaccion));
    }
}
