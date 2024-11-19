package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Detalle;
import com.backus.proyecto.modelo.Carrito;
import com.backus.proyecto.entity.Producto;
import com.backus.proyecto.modelo.ProductoRequest;
import com.backus.proyecto.services.DetalleService;
import com.backus.proyecto.services.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("/carro")
    public String listarMarcas(Model model) {

        return "formulario/cliente/carrito/carrito";
    }
    @PostMapping("/agregar/{id}")
    public ResponseEntity<String> agregarAlCarrito(@PathVariable Integer id, HttpSession session) {
        // Verificar si el producto existe
        Producto producto = productoService.buscarPorId(id).orElse(null);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        // Obtener o crear el carrito de la sesión del usuario
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute("carrito", carrito);
        }

        // Verificar si el producto ya está en el carrito
        ProductoRequest productoRequest = encontrarProductoEnCarrito(carrito, producto.getIdProducto());

        if (productoRequest == null) {
            // Si no está en el carrito, crear un nuevo ProductoRequest
            productoRequest = new ProductoRequest();
            productoRequest.setIdProducto(producto.getIdProducto());
            productoRequest.setNombre(producto.getNombre());
            productoRequest.setCantidad(1);
            productoRequest.setPrecioUnitario(producto.getPrecio());
            productoRequest.setPrecioTotal(productoRequest.getPrecioUnitario() * productoRequest.getCantidad());

            carrito.agregarProducto(productoRequest);
        } else {
            // Si está en el carrito, incrementar la cantidad
            productoRequest.setCantidad(productoRequest.getCantidad() + 1);
            productoRequest.setPrecioTotal(productoRequest.getPrecioUnitario() * productoRequest.getCantidad());
        }

        // Actualizar el carrito en la sesión
        session.setAttribute("carrito", carrito);

        // Devolver una respuesta con mensaje de éxito
        return ResponseEntity.ok("Producto agregado al carrito");
    }

    // Método auxiliar para encontrar un producto en el carrito por su ID
    private ProductoRequest encontrarProductoEnCarrito(Carrito carrito, Integer idProducto) {
        for (ProductoRequest producto : carrito.getProductos()) {
            if (producto.getIdProducto().equals(idProducto)) {
                return producto;
            }
        }
        return null;
    }

    @PostMapping("/disminuir/{id}")
    public ResponseEntity<String> disminuirCantidadProducto(@PathVariable Integer id, HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null || carrito.getProductos().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Buscar el producto en el carrito y disminuir la cantidad si es mayor que 1
        for (ProductoRequest producto : carrito.getProductos()) {
            if (producto.getIdProducto().equals(id)) {
                if (producto.getCantidad() > 1) {
                    producto.setCantidad(producto.getCantidad() - 1);
                    producto.setPrecioTotal(producto.getPrecioUnitario() * producto.getCantidad());
                }
                break;
            }
        }

        // Actualizar el carrito en la sesión
        session.setAttribute("carrito", carrito);

        return ResponseEntity.ok("Cantidad del producto disminuida en el carrito");
    }

    @PostMapping("/aumentar/{id}")
    public ResponseEntity<String> aumentarCantidadProducto(@PathVariable Integer id, HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null || carrito.getProductos().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean productoEncontrado = false;
        // Buscar el producto en el carrito y aumentar la cantidad
        for (ProductoRequest producto : carrito.getProductos()) {
            if (producto.getIdProducto().equals(id)) {
                producto.setCantidad(producto.getCantidad() + 1);
                producto.setPrecioTotal(producto.getPrecioUnitario() * producto.getCantidad());
                productoEncontrado = true;
                break;
            }
        }

        // Si no se encontró el producto, devolver not found
        if (!productoEncontrado) {
            return ResponseEntity.notFound().build();
        }

        // Actualizar el carrito en la sesión
        session.setAttribute("carrito", carrito);

        return ResponseEntity.ok("Cantidad del producto aumentada en el carrito");
    }

    @PostMapping("/vaciar")
    public ResponseEntity<String> vaciarCarrito(HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.getProductos().clear(); // Vaciar la lista de productos del carrito
        }
        session.removeAttribute("carrito"); // Eliminar el carrito de la sesión

        return ResponseEntity.ok("Carrito vaciado");
    }

    @PostMapping("/retirar/{id}")
    public ResponseEntity<String> retirarProducto(@PathVariable Integer id, HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null || carrito.getProductos().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Buscar y remover el producto del carrito
        Iterator<ProductoRequest> iterator = carrito.getProductos().iterator();
        while (iterator.hasNext()) {
            ProductoRequest producto = iterator.next();
            if (producto.getIdProducto().equals(id)) {
                iterator.remove();
                break;
            }
        }

        // Actualizar el carrito en la sesión
        session.setAttribute("carrito", carrito);

        return ResponseEntity.ok("Producto retirado del carrito");
    }

    @GetMapping("/cantidad")
    @ResponseBody
    public ResponseEntity<Integer> obtenerCantidadProductos(HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            return ResponseEntity.ok(0); // Si no hay carrito, retornar 0
        }
        return ResponseEntity.ok(carrito.getCantidadTiposProductos());
    }


    @GetMapping("/resumenCarrito")
    @ResponseBody
    public ResponseEntity<List<ProductoRequest>> obtenerResumenCarrito(HttpSession session) {
        System.err.println("Obteniendo resumen del carrito");
        System.err.println(session.getAttribute("carrito"));
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null || carrito.getProductos().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<ProductoRequest> productos = carrito.getProductos();
        System.err.println(productos);
        return ResponseEntity.ok(productos);
    }



}
