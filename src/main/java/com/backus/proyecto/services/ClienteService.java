package com.backus.proyecto.services;

import com.backus.proyecto.entity.Cliente;
import com.backus.proyecto.repository.ClienteRepository;
import com.backus.proyecto.repository.EmpleadoRepository;
import com.backus.proyecto.util.interfaces.CrudService;
import com.backus.proyecto.util.interfaces.CrudServiceLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ClienteService implements CrudServiceLong <Cliente> {
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listar() {
        return  clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public Cliente guardar(Cliente request) {
        return null;
    }

    @Override
    public boolean existePorId(Long id) {
        return false;
    }

    @Override
    public void eliminarPorId(Long id) {

    }
}
