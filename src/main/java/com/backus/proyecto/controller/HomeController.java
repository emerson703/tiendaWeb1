package com.backus.proyecto.controller;

import com.backus.proyecto.entity.*;
import com.backus.proyecto.modelo.Carrito;
import com.backus.proyecto.modelo.ProductoRequest;
import com.backus.proyecto.repository.EmpleadoRepository;
import com.backus.proyecto.services.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequestMapping({"", "/", "/home"})
@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private CalificacionService calificacionService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    DetalleService detalleService;

    private static final String RUTA_VISTA = "home/";
    private static final String RUTA_BASE = "redirect:/";

    @GetMapping()
    public String listarMarcas(Model model) {
        model.addAttribute("titulo", "Listado de productos");
        model.addAttribute("listadoProducto", productoService.productosActivos());
        //model.addAttribute("tituloCalificacion", "Listado de Calificaciones");
        //model.addAttribute("listCalificacion", calificacionService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return RUTA_VISTA + "index";
    }

    @GetMapping("/portal")
    public String trabajadores() {
        return "formulario/trabajador/portal/login";
    }


    @GetMapping("/verProducto/{id}")
    public String detalleProducto(Model model, @PathVariable Integer id) {
        model.addAttribute("titulo", "Detalle del Producto");

        Producto producto = productoService.buscarPorId(id).orElse(null);

        if (producto == null || !producto.isVisible()) {
            return RUTA_BASE;
        }

        model.addAttribute("producto", producto);
        return RUTA_VISTA + "detalleProducto";
    }

    @GetMapping("/detalleCarrito")
    public String verDetalleCarrito(Model model, HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null || carrito.getProductos().isEmpty()) {
            model.addAttribute("titulo", "Carrito de Compras Vacío");
            model.addAttribute("mensaje", "Tu carrito de compras está vacío.");
            return RUTA_VISTA + "detalleCarritoVacio"; // Vista para carrito vacío
        }

        // Obtener información del carrito
        List<ProductoRequest> productos = carrito.getProductos();
        int cantidadTotal = carrito.getCantidadTotalProductos();
        double sumatoriaTotal = carrito.getSumatoriaTotal();

        // Pasar la información a la vista
        model.addAttribute("titulo", "Detalle del Carrito");
        model.addAttribute("productos", productos);
        model.addAttribute("cantidadTotal", cantidadTotal);
        model.addAttribute("sumatoriaTotal", sumatoriaTotal);

        return RUTA_VISTA + "detalleCarrito"; // Vista para mostrar el detalle del carrito
    }

    @GetMapping("/infoCarrito")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerInfoCarrito(HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null || carrito.getProductos().isEmpty()) {
            // Si el carrito está vacío, devolver un objeto JSON con cantidad 0 y total 0.00
            Map<String, Object> response = new HashMap<>();
            response.put("cantidadTotal", 0);
            response.put("totalAPagar", 0.00);
            return ResponseEntity.ok(response);
        }

        // Obtener la cantidad total de productos en el carrito
        int cantidadTotal = carrito.getCantidadTotalProductos();
        // Obtener el total a pagar en el momento
        double totalAPagar = carrito.getSumatoriaTotal();

        // Construir la respuesta JSON
        Map<String, Object> response = new HashMap<>();
        response.put("cantidadTotal", cantidadTotal);
        response.put("totalAPagar", totalAPagar);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/procesar")
    public String procesar(Model model, HttpSession session) {
        model.addAttribute("titulo", "Procesar");
        return RUTA_VISTA + "procesar";
    }

    @PostMapping("/procesarCarrito")
    @ResponseBody
    public String procesarCarrito(@RequestParam("metodoPago") String metodoPago, HttpSession session, Model model) {
        System.err.println("Metodo de pago: " + metodoPago);
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            throw new IllegalStateException("El carrito no está presente en la sesión.");
        }
        Pedido pedido = new Pedido();
        pedido.setCantidadP(carrito.getCantidadTotalProductos());
        pedido.setMetodoPago(metodoPago);
        pedido.setFechaOperacion(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        session.getAttribute("carrito");
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if(cliente.getIdCliente()==1){
            pedido.setAnonimoNombre(cliente.getNombre());
            pedido.setAnonimoEmail(cliente.getEmail());
            pedido.setAnonimoTelefono(cliente.getTelefono());
        }
        pedido.setCliente(cliente);
        session.setAttribute("cliente", cliente);
        pedido = pedidoService.guardar(pedido);

        for (ProductoRequest productoRequest : carrito.getProductos()) {
            Detalle detalle = new Detalle();
            detalle.setPedido(pedido);  // Asignar el objeto Pedido, no solo el ID
            Producto producto = new Producto();
            producto.setIdProducto(productoRequest.getIdProducto());
            detalle.setProducto(producto);  // Asignar el producto completo
            detalle.setCantidad(productoRequest.getCantidad());
            detalle.setPrecio(productoRequest.getPrecioUnitario());
            detalle.setSubtotal(productoRequest.getCantidad() * productoRequest.getPrecioUnitario());
            detalleService.guardar(detalle);
        }
        // Eliminar el carrito de la sesión
        session.removeAttribute("carrito");

        // Determinar el destinatario según si el cliente es registrado o anónimo
        String destinatarioEmail = (cliente.getIdCliente() == 1) ? pedido.getAnonimoEmail() : cliente.getEmail();
        String asunto = "Confirmación de Pedido #" + pedido.getIdPedido();
        String mensaje = "Gracias por realizar un pedido con nosotros. Su número de pedido es: " + pedido.getIdPedido() +
                ". Su estado actual es: " + pedido.getEstado() + ". Le notificaremos cualquier cambio en el estado del pedido.";

        // Enviar notificación por correo al cliente
        emailService.enviarNotificacionPedido(destinatarioEmail, asunto, mensaje);


        // Obtener lista de empleados y enviarles la notificación
        List<Empleado> empleados = empleadoRepository.findAll();
        String mensajeEmpleado = "Se ha generado un nuevo pedido con ID: " + pedido.getIdPedido();
        for (Empleado empleado : empleados) {
            emailService.enviarNotificacionPedido(empleado.getCorreo(), "Nuevo Pedido Generado", mensajeEmpleado);
        }
        // Añadir el número de pedido al modelo
         return pedido.getIdPedido().toString();

    }


}
