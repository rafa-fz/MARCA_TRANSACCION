package com.banquito.marca.aplicacion.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "Transaccion")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tarjeta_id")
    private Integer tarjetaId;

    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaHora;

    private BigDecimal valor;
    private BigDecimal comision;
    private Boolean esDiferido;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "tarjeta_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Tarjeta tarjeta;
}
