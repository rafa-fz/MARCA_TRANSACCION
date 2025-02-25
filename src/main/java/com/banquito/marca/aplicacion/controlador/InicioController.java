package com.banquito.marca.aplicacion.controlador;

import com.banquito.marca.compartido.utilidades.UtilidadRespuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
@Tag(name = "Inicio API", description = "Inicio API")
public class InicioController {

    @GetMapping
    @Operation(
            summary = "Endpoint de inicio",
            description = "Este endpoint proporciona información inicial de la API Marca."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> inicio() {
        Map<String, String> datos = new HashMap<>();
        datos.put("mensaje", "API Marca");

        return ResponseEntity.ok().body(UtilidadRespuesta.exito(datos));
    }
}
