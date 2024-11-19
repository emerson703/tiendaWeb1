package com.backus.proyecto.util.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudServiceLong <T>{
    List<T> listar();
    Optional<T> buscarPorId(Long id);
    T guardar(T request);
    boolean existePorId(Long id);
    void eliminarPorId(Long id);
}
