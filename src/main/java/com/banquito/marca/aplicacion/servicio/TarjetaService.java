package com.banquito.marca.aplicacion.servicio;

import com.banquito.marca.aplicacion.modelo.Cliente;
import com.banquito.marca.aplicacion.modelo.Tarjeta;
import com.banquito.marca.aplicacion.repositorio.ITarjetaRepository;
import com.banquito.marca.compartido.excepciones.EntidadNoEncontradaExcepcion;
import com.banquito.marca.compartido.excepciones.OperacionInvalidaExcepcion;
import com.banquito.marca.compartido.utilidades.UtilidadHash;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TarjetaService {
    private final ITarjetaRepository repositorio;

    private final GeneradorTarjetaService generadorTarjetaService;
    private final ValidadorTarjetasService validadorTarjetasService;

    private static final String TIPO_CREDITO = "CRD";
    private static final  String TIPO_DEBITO = "DEB";

    private static final String VISA = "VISA";
    private static final String MASTERCARD = "MASTERCARD";

    public TarjetaService(
            ITarjetaRepository repositorio,
            GeneradorTarjetaService generadorTarjetaService,
            ValidadorTarjetasService validadorTarjetasService
    ) {
        this.repositorio = repositorio;

        this.generadorTarjetaService = generadorTarjetaService;
        this.validadorTarjetasService = validadorTarjetasService;
    }

    public Tarjeta buscarPorNuemro(String numero) {
        return this.repositorio.findByNumero(numero)
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("No una tarjeta con número: " + numero));
    }

    public void crearTarjeta(Tarjeta tarjeta, Cliente cliente, String franquicia) {
        if (!tarjeta.getTipo().equals(TIPO_CREDITO) && !tarjeta.getTipo().equals(TIPO_DEBITO))
            throw new OperacionInvalidaExcepcion("Tipo de tarjeta invalida");

        switch (franquicia) {
            case TarjetaService.VISA -> {
                tarjeta.setNumero(this.generadorTarjetaService.generarNumeroTarjetaVisaValido());
            }
            case TarjetaService.MASTERCARD -> {
                tarjeta.setNumero(this.generadorTarjetaService.generarNumeroTarjetaMasterCardValido());
            }
            default -> {
                throw new OperacionInvalidaExcepcion("Franquicia invalida");
            }
        }

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaCaducidad = fechaActual.plusYears(5).withDayOfMonth(fechaActual.plusYears(5).lengthOfMonth());

        tarjeta.setCvv(UtilidadHash.hashString(tarjeta.getCvv()));
        tarjeta.setFechaCaducidad(fechaCaducidad);
        tarjeta.setFechaEmision(LocalDateTime.now());

        tarjeta.setClienteId(cliente.getId());

        repositorio.save(tarjeta);
    }
}
