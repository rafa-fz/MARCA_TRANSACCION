package com.banquito.marca.aplicacion.repositorio;

import com.banquito.marca.aplicacion.modelo.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransaccionRepository extends JpaRepository<Transaccion, Integer> {
}
