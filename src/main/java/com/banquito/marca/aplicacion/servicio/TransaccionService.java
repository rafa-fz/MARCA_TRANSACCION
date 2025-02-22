package com.banquito.marca.aplicacion.servicio;

import com.banquito.marca.aplicacion.modelo.Tarjeta;
import com.banquito.marca.aplicacion.modelo.Transaccion;
import com.banquito.marca.aplicacion.repositorio.ITransaccionRepository;
import com.banquito.marca.compartido.excepciones.OperacionInvalidaExcepcion;
import com.banquito.marca.compartido.utilidades.UtilidadHash;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class TransaccionService {
    private final ITransaccionRepository repositorio;

    private final ValidadorTarjetasService validadorTarjetasService;

    private static final String ESTADO_PENDIENTE = "PEN";
    private static final String ESTADO_APROVADA = "APR";
    private static final String ESTADO_RECHAZADA = "REC";
    private static final String ESTADO_REVERSADA = "REV";

    private static final BigDecimal PORCENTAJE_COMISION = BigDecimal.valueOf(0.001);

    public TransaccionService(
            ITransaccionRepository repositorio,
            ValidadorTarjetasService validadorTarjetasService
    ) {
        this.repositorio = repositorio;
        this.validadorTarjetasService = validadorTarjetasService;
    }

    public void registrarTransaccion(Transaccion transaccion, Tarjeta tarjeta, String cvv, String fechaCaducidad) {
        if (!UtilidadHash.verificarString(cvv, tarjeta.getCvv()))
            throw new OperacionInvalidaExcepcion("Código de seguridad de la tarjeta incorrecto");

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth fechaEntrada = YearMonth.parse(fechaCaducidad, inputFormatter);;
        YearMonth fechaBaseDatos = YearMonth.from(tarjeta.getFechaCaducidad());

        if (!fechaEntrada.equals(fechaBaseDatos))
            throw new OperacionInvalidaExcepcion("La fecha de caducidad de la tarjeta incorrecta");

        transaccion.setTarjetaId(tarjeta.getId());
        transaccion.setEstado(TransaccionService.ESTADO_PENDIENTE);
        transaccion.setFechaHora(LocalDateTime.now());
        transaccion.setComision(this.calcularComision(transaccion.getValor()));
        transaccion.setFechaActualizacion(LocalDateTime.now());

        this.repositorio.save(transaccion);
    }

    private BigDecimal calcularComision(BigDecimal valor) {
        return valor.multiply(PORCENTAJE_COMISION);
    }
}
