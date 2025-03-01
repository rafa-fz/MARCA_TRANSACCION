package com.banquito.marca.aplicacion.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.banquito.marca.aplicacion.modelo.Transaccion;

@Repository
public interface TransaccionRepository extends MongoRepository<Transaccion, String> {

}
