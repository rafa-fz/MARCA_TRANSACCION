package com.banquito.marca.aplicacion.servicio;

import com.banquito.marca.aplicacion.modelo.Cliente;
import com.banquito.marca.aplicacion.repositorio.IClienteRepository;
import com.banquito.marca.compartido.excepciones.EntidadDuplicadaExcepcion;
import com.banquito.marca.compartido.excepciones.EntidadNoEncontradaExcepcion;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {
    private final IClienteRepository repositorio;

    public ClienteService(IClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Cliente buscarPorId(Integer id) {
        return this.repositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("No existe ningún cliente con ID: " + id));
    }

    public Cliente buscarPorIdentificacionOFallar(String identificacion) {
        return this.repositorio.findByIdentificacion(identificacion)
                .orElseThrow(() -> new EntidadNoEncontradaExcepcion("No existe ningún cliente con número de identificación: " + identificacion));
    }

    public Cliente buscarPorIdentificacion(String identificacion) {
        return this.repositorio.findByIdentificacion(identificacion).orElse(null);
    }

    public void registrarCliente(Cliente cliente) {
        if (this.repositorio.findByIdentificacion(cliente.getIdentificacion()).isPresent())
            throw new EntidadDuplicadaExcepcion("Ya existe un cliente con el número de identificación: " + cliente.getIdentificacion());

        this.repositorio.save(cliente);
    }

    public void actualizarCliente(Cliente cliente) {
        Optional<Cliente> opAuxCliente = this.repositorio.findByIdentificacion(cliente.getIdentificacion());

        if (opAuxCliente.isPresent() && !opAuxCliente.get().equals(cliente))
            throw new EntidadDuplicadaExcepcion("Ya existe un cliente con el número de identificación: " + cliente.getIdentificacion());

        this.repositorio.save(cliente);
    }
}
