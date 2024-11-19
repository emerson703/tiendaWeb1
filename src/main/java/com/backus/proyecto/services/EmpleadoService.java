package com.backus.proyecto.services;

import com.backus.proyecto.entity.Empleado;
import com.backus.proyecto.repository.EmpleadoRepository;
import com.backus.proyecto.util.interfaces.CrudServiceLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EmpleadoService implements CrudServiceLong <Empleado> {
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Override
    public List<Empleado> listar() {
        return  empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> buscarPorId(Long id) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(id);
        return empleadoOptional.isPresent() ? Optional.of(empleadoOptional.get()) : Optional.empty();
    }

    @Override
    public Empleado guardar(Empleado entity) {

        return  empleadoRepository.save(entity);
    }


    @Override
    public boolean existePorId(Long id) {
        return empleadoRepository.findById(id).isPresent();

    }

    @Override
    public void eliminarPorId(Long id) {
        empleadoRepository.deleteById(id);
    }


}
