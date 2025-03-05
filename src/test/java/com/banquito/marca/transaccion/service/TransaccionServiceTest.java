package com.banquito.marca.transaccion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banquito.marca.transaccion.client.TarjetaClient;
import com.banquito.marca.transaccion.controller.dto.ProcesadorRespuestaDTO;
import com.banquito.marca.transaccion.controller.dto.SwiftResponseDTO;
import com.banquito.marca.transaccion.controller.dto.TransaccionPeticionDTO;
import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaRequestDTO;
import com.banquito.marca.transaccion.controller.dto.ValidacionTarjetaResponseDTO;
import com.banquito.marca.transaccion.excepcion.TransaccionNotFoundException;
import com.banquito.marca.transaccion.model.Transaccion;
import com.banquito.marca.transaccion.repository.TransaccionRepository;
import com.banquito.marca.transaccion.utils.EncriptacionUtil;

import feign.FeignException;
import feign.Request;

@ExtendWith(MockitoExtension.class)
public class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private TarjetaClient tarjetaClient;

    @Mock
    private EncriptacionUtil encriptacionUtil;

    @InjectMocks
    private TransaccionService transaccionService;

    private TransaccionPeticionDTO peticionDTO;
    private ValidacionTarjetaResponseDTO respuestaValidacion;
    private SwiftResponseDTO respuestaSwift;

    @BeforeEach
    void setUp() {
        peticionDTO = new TransaccionPeticionDTO();
        peticionDTO.setCodigoUnicoTransaccion("TRX123");
        peticionDTO.setNumeroTarjeta("5135860771089499");
        peticionDTO.setCvv("553");
        peticionDTO.setFechaCaducidad("03/29");
        peticionDTO.setMonto(new BigDecimal("100.50"));

        respuestaValidacion = new ValidacionTarjetaResponseDTO();
        respuestaSwift = new SwiftResponseDTO();
    }

    @Test
    void validarTarjeta_CuandoTarjetaEsValida_DebeRetornarRespuestaExitosa() {
        respuestaValidacion.setEsValida(true);
        respuestaValidacion.setMensaje("La tarjeta es válida");
        respuestaSwift.setSwiftBanco("PICHECU0001");

        when(tarjetaClient.validarTarjeta(any(ValidacionTarjetaRequestDTO.class))).thenReturn(respuestaValidacion);
        when(tarjetaClient.obtenerSwift(anyString())).thenReturn(respuestaSwift);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(new Transaccion());

        ProcesadorRespuestaDTO resultado = transaccionService.validarTarjeta(peticionDTO);

        assertTrue(resultado.isEsValida());
        assertEquals("La tarjeta es válida", resultado.getMensaje());
        assertEquals("PICHECU0001", resultado.getSwiftBanco());
    }

    @Test
    void validarTarjeta_CuandoTarjetaNoEsValida_DebeRetornarRespuestaFallida() {
        respuestaValidacion.setEsValida(false);
        respuestaValidacion.setMensaje("Tarjeta inválida");

        when(tarjetaClient.validarTarjeta(any(ValidacionTarjetaRequestDTO.class))).thenReturn(respuestaValidacion);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(new Transaccion());

        ProcesadorRespuestaDTO resultado = transaccionService.validarTarjeta(peticionDTO);

        assertFalse(resultado.isEsValida());
        assertEquals("Tarjeta inválida", resultado.getMensaje());
    }

    @Test
    void validarTarjeta_CuandoHayErrorDeValidacion_DebeRetornarRespuestaConError() {
        Request request = Request.create(Request.HttpMethod.POST, "/validar", new HashMap<>(), null, null, null);
        FeignException.BadRequest badRequest = new FeignException.BadRequest("Error de validación", request, null,
                Collections.emptyMap());

        when(tarjetaClient.validarTarjeta(any(ValidacionTarjetaRequestDTO.class))).thenThrow(badRequest);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(new Transaccion());

        ProcesadorRespuestaDTO resultado = transaccionService.validarTarjeta(peticionDTO);

        assertFalse(resultado.isEsValida());
        assertNotNull(resultado.getMensaje());
    }

    @Test
    void validarTarjeta_CuandoErrorAlObtenerSwift_DebeRetornarRespuestaConSwiftNA() {
        respuestaValidacion.setEsValida(true);
        respuestaValidacion.setMensaje("La tarjeta es válida");
        Request request = Request.create(Request.HttpMethod.GET, "/swift", new HashMap<>(), null, null, null);
        FeignException.NotFound notFound = new FeignException.NotFound(
                "Tarjeta no encontrada",
                request,
                "{\"statusCode\": 404, \"message\": \"Tarjeta no encontrada\"}".getBytes(),
                Collections.emptyMap());

        when(tarjetaClient.validarTarjeta(any(ValidacionTarjetaRequestDTO.class))).thenReturn(respuestaValidacion);
        when(tarjetaClient.obtenerSwift(anyString())).thenThrow(notFound);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(new Transaccion());

        ProcesadorRespuestaDTO resultado = transaccionService.validarTarjeta(peticionDTO);

        assertNotNull(resultado.getMensaje());
    }

    @Test
    void buscarTransaccion_CuandoExisteTransaccion_DebeRetornarTransaccion() {
        String codigoUnico = "TRX123";
        Transaccion transaccion = new Transaccion();
        transaccion.setCodigoUnicoTransaccion(codigoUnico);

        when(transaccionRepository.findById(codigoUnico)).thenReturn(java.util.Optional.of(transaccion));

        Transaccion resultado = transaccionService.buscarTransaccion(codigoUnico);

        assertNotNull(resultado);
        assertEquals(codigoUnico, resultado.getCodigoUnicoTransaccion());
    }

    @Test
    void buscarTransaccion_CuandoNoExisteTransaccion_DebeLanzarExcepcion() {
        String codigoUnico = "TRX123";
        when(transaccionRepository.findById(codigoUnico)).thenReturn(java.util.Optional.empty());

        assertThrows(TransaccionNotFoundException.class, () -> {
            transaccionService.buscarTransaccion(codigoUnico);
        });
    }
}