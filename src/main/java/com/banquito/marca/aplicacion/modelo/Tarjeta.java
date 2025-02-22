package com.banquito.marca.aplicacion.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id")
    private Integer clienteId;

    private String tipo;
    private String numero;
    private String cvv;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaCaducidad;

    private LocalDateTime fechaEmision;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Cliente cliente;
}
