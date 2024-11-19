package com.backus.proyecto.services;

import com.backus.proyecto.entity.Pedido;
import com.backus.proyecto.repository.PedidoRepository;
import com.backus.proyecto.util.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service

public class PedidoService implements CrudService<Pedido> {
    @Autowired
    private PedidoRepository PedidoRepository;
    @Override
    public List<Pedido> listar() {
        return  PedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> buscarPorId(Integer id) {
        Optional<Pedido> PedidoOptional = PedidoRepository.findById(id);
        if (PedidoOptional.isPresent()) {
            return Optional.of(PedidoOptional.get());
        } else {
            return Optional.empty(); // Devolver un Optional vacío en caso de que no se encuentre la categoría
        }
    }

    public List<Pedido> buscarPorClienteId(Integer idCliente) {
        return PedidoRepository.findByClienteIdCliente(idCliente);
    }

    @Override
    public Pedido guardar(Pedido entity) {
        return  PedidoRepository.save(entity);
    }


    @Override
    public boolean existePorId(Integer id) {
        return PedidoRepository.findById(id).isPresent();

    }

    @Override
    public void eliminarPorId(Integer id) {
        PedidoRepository.deleteById(id);
    }


}
