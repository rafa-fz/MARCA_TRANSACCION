package com.banquito.marca.transaccion.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.banquito.marca.transaccion.model.Transaccion;

@Repository
public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
}