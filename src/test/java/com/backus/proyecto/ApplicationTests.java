package com.backus.proyecto;// ProductoServiceTest.java


import com.backus.proyecto.entity.Producto;
import com.backus.proyecto.repository.ProductoRepository;
import com.backus.proyecto.services.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

	@Mock // Anotación para simular el repositorio de productos
	private ProductoRepository productoRepository;

	@InjectMocks // Inyecta el repositorio simulado en el servicio
	private ProductoService productoService;

	@BeforeEach
	void setUp() {
		// Inicializa los mocks antes de cada prueba
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGuardarProducto_CasoOk() {
		// Crear un producto para probar
		Producto producto = new Producto();
		producto.setNombre("Cerveza");
		producto.setPrecio(10.0);

		// Simular el comportamiento del repositorio
		when(productoRepository.save(producto)).thenReturn(producto);

		// Llamar al método guardar y verificar el resultado
		Producto resultado = productoService.guardar(producto);
		assertEquals("Cerveza", resultado.getNombre()); // Verifica el nombre
		assertEquals(10.0, resultado.getPrecio()); // Verifica el precio
	}

	@Test
	void testBuscarPorId_CasoOk() {
		// Crear un producto para simular
		Producto producto = new Producto();
		producto.setNombre("Cerveza");
		producto.setPrecio(15.0);

		// Simular el comportamiento del repositorio
		when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

		// Llamar al método buscarPorId y verificar el resultado
		Optional<Producto> resultado = productoService.buscarPorId(1);
		assertTrue(resultado.isPresent()); // Verifica que el producto está presente
		assertEquals("Cerveza", resultado.get().getNombre()); // Verifica el nombre
	}

	@Test
	void testBuscarPorId_CasoFallido() {
		// Simular que no se encuentra un producto con ID 99
		when(productoRepository.findById(99)).thenReturn(Optional.empty());

		// Llamar al método buscarPorId y verificar el resultado
		Optional<Producto> resultado = productoService.buscarPorId(99);
		assertFalse(resultado.isPresent()); // Verifica que el resultado es vacío
	}

	@Test
	void testEliminarPorId_CasoOk() {
		// ID del producto a eliminar
		Integer id = 1;

		// No hay necesidad de simular el comportamiento ya que solo se prueba el método
		productoService.eliminarPorId(id);

		// Verifica que el método deleteById fue llamado una vez con el ID proporcionado
		verify(productoRepository, times(1)).deleteById(id);
	}

	@Test
	void testProductosActivos_CasoOk() {
		// Crear productos de prueba
		Producto p1 = new Producto();
		p1.setNombre("Cerveza");
		p1.setPrecio(5.0);
		p1.setVisible(true); // Este producto es visible

		Producto p2 = new Producto();
		p2.setNombre("Gaseosa");
		p2.setPrecio(15.0);
		p2.setVisible(false); // Este producto no es visible

		// Simular el comportamiento del repositorio
		when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

		// Llamar al método productosActivos y verificar el resultado
		List<Producto> resultado = productoService.productosActivos();
		assertEquals(1, resultado.size()); // Debe retornar solo un producto visible
		assertEquals("Cerveza", resultado.get(0).getNombre()); // Verifica el nombre del producto visible
	}




}
