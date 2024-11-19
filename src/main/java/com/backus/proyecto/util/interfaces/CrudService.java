package com.backus.proyecto.util.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
	List<T> listar();
	Optional<T> buscarPorId(Integer id);
	T guardar(T request)  ;
	boolean existePorId(Integer id);
	void eliminarPorId(Integer id);

}
