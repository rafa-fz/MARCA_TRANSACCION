package com.banquito.marca.aplicacion.repositorio;

import com.banquito.marca.aplicacion.modelo.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITarjetaRepository extends JpaRepository<Tarjeta, Integer> {
    Optional<Tarjeta> findByNumero(String numero);
    Optional<Tarjeta> findTopByOrderByFechaEmisionDesc();
}
