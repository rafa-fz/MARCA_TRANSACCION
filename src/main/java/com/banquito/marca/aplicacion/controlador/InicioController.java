package com.banquito.marca.aplicacion.controlador;

import com.banquito.marca.compartido.utilidades.UtilidadRespuesta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InicioController {
    @GetMapping
    public ResponseEntity<?> inicio() {
        Map<String, String> datos = new HashMap<>();
        datos.put("mensaje", "API Marca");

        return ResponseEntity.ok().body(UtilidadRespuesta.exito(datos));
    }
}
