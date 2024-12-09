package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Cliente;
import com.backus.proyecto.repository.ClienteRepository;
import com.backus.proyecto.services.ClienteService;
import com.backus.proyecto.services.DetalleService;

import com.backus.proyecto.services.PedidoService;
import com.backus.proyecto.services.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private DetalleService detalleService;
    @Autowired
    private ProductoService productoService;

    private Cliente obtenerClienteDeSesion(HttpSession session) {
        return (Cliente) session.getAttribute("cliente");
    }

    @PostMapping("/anonimo")
    @ResponseBody
    public Map<String, Object> capturarCorreo(@RequestBody Map<String, String> params, HttpSession session) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);//default
        cliente.setEmail(params.get("email"));
        cliente.setNombre(params.get("nombre"));
        cliente.setTelefono(params.get("telefono"));
        Map<String, Object> response = new HashMap<>();
        session.setAttribute("cliente", cliente);
        response.put("success", true);
        return response;
    }
    @GetMapping("/listar")
    public String listarEmpleados(Model model) {
        model.addAttribute("titulo", "Lista de Clientes");
        model.addAttribute("listCliente", clienteService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return "/formulario/trabajador/cliente/index"; //
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> loginSubmit(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get("email");
        String clave = params.get("clave");
        System.err.println("Login submit: " + email + " " + clave);

        Map<String, Object> response = new HashMap<>();
        Cliente cliente = clienteRepository.findByEmailAndClave(email, clave);
        if (cliente != null) {
            session.setAttribute("cliente", cliente);
            response.put("success", true);
            System.err.println("Cliente logueado: " + cliente.getNombre());
        } else {
            response.put("success", false);
            response.put("error", "Correo o clave incorrectos");
        }

        return response;
    }

    @PostMapping("/loginX")
    public String login(@RequestParam String email, @RequestParam String clave, HttpSession session) {
        try {
            Cliente cliente = clienteRepository.findByEmailAndClave(email, clave);
            if(cliente == null) {
                throw new RuntimeException("Correo o clave incorrectos");
            }
            session.setAttribute("cliente", cliente);

            return "redirect:/cliente/perfil";
        } catch (Exception e) {
            throw new RuntimeException("Error al loguear");
            
        }

    }
    @GetMapping("/productos")
    public String listarMarcas(Model model) {
        model.addAttribute("titulo", "Listado de productos");
        model.addAttribute("listadoProducto", productoService.productosActivos());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return "formulario/cliente/portal/productos";
    }

    @GetMapping("/datos")
    public String mostrarDatos(Model model, HttpSession session) {
        Cliente cliente = obtenerClienteDeSesion(session);
        if (cliente != null) {
            model.addAttribute("cliente", clienteRepository.findByEmail(cliente.getEmail()));
            return "historialPedidos";
        } else {
            return "redirect:/"; // Redirect to login page
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/"; // Redirect to login page
    }


    // Método para mostrar el perfil del cliente
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, HttpSession session) {
        Cliente cliente = obtenerClienteDeSesion(session); // Lógica para obtener el cliente actual
        model.addAttribute("cliente", clienteRepository.findByEmail(cliente.getEmail()));
        return "formulario/cliente/portal/perfil";
    }

    // Método para mostrar la vista de edición del perfil
    @GetMapping("/editarPerfil")
    public String editarPerfil(Model model, HttpSession session) {
        Cliente cliente = obtenerClienteDeSesion(session);// Lógica para obtener el cliente actual
        model.addAttribute("cliente", clienteRepository.findByEmail(cliente.getEmail()));
        return "formulario/cliente/portal/editarPerfil";
    }

    // Método para procesar la edición del perfil
    @PostMapping("/editarPerfil")
    public String guardarCambios(@ModelAttribute("cliente") Cliente cliente, Model model, HttpSession session) {
        // Lógica para guardar los cambios en el perfil
        Cliente clientex = obtenerClienteDeSesion(session);// Lógica para obtener el cliente actual
        cliente.setEmail(clientex.getEmail());
        clienteRepository.save(cliente);
        model.addAttribute("mensaje", "Perfil actualizado exitosamente");
        return "formulario/cliente/portal/perfil";
    }

    @GetMapping("/historialPedido")
    public String historialPedido(Model model, HttpSession session) {
        Cliente cliente = obtenerClienteDeSesion(session);

        if (cliente != null) {
            model.addAttribute("titulo", "Historial de pedidos");
            model.addAttribute("cliente", cliente);
            model.addAttribute("pedidos", pedidoService.buscarPorClienteId(cliente.getIdCliente()));
            return "formulario/cliente/portal/historial";
        } else {
            return "redirect:/cliente/login";
        }
    }

    @PostMapping("/agregar")
    public String agregarCliente(
            @RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam("clave") String clave,
            @RequestParam("telefono") String telefono,
            Model model, HttpSession session) {

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setEmail(email);
        cliente.setClave(clave);
        cliente.setTelefono(telefono);

        cliente = clienteRepository.save(cliente);
        session.setAttribute("cliente", cliente);
        model.addAttribute("mensaje", "Cliente agregado exitosamente");

        return "redirect:/cliente/perfil";
    }
    @GetMapping("/mapa")
    public String mapas(Model model) {
        model.addAttribute("titulo", "Ubicacion del Cliente");
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return "/formulario/cliente/portal/mapa"; //
    }


}
