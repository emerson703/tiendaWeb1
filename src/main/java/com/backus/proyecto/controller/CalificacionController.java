package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Calificacion;
import com.backus.proyecto.entity.Cliente;
import com.backus.proyecto.entity.Empleado;
import com.backus.proyecto.entity.Pedido;
import com.backus.proyecto.repository.CalificacionRepository;
import com.backus.proyecto.repository.ClienteRepository;
import com.backus.proyecto.repository.PedidoRepository;
import com.backus.proyecto.services.CalificacionService;
import com.backus.proyecto.services.ClienteService;
import com.backus.proyecto.services.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;


@Controller
@RequestMapping("/calificacion")
public class CalificacionController {
    @Autowired
    private CalificacionService calificacionService;
    @Autowired
    private CalificacionRepository calificacionRepository;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteRepository clienteRepository;
    @GetMapping("/listar")
    public String listarCalificaciones(Model model) {
        model.addAttribute("titulo", "Lista de Calificaciones por parte de los clientes");
        model.addAttribute("listCalificacion", calificacionService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return "/formulario/trabajador/calificacion/index"; //
    }
    @PostMapping("/guardar")
    public String guardarCalificacion(@RequestParam int idPedido,
                                      @RequestParam Long idCliente,
                                      @RequestParam int calificacion,
                                      @RequestParam(required = false) String comentario,
                                      RedirectAttributes redirectAttributes) {

        // Crear una nueva calificación
        Calificacion nuevaCalificacion = new Calificacion();

        // Buscar entidades relacionadas
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // Asignar valores
        nuevaCalificacion.setPedido(pedido);
        nuevaCalificacion.setCliente(cliente);
        nuevaCalificacion.setCalificacion(calificacion);
        nuevaCalificacion.setComentario(comentario);

        // Guardar la calificación
        calificacionRepository.save(nuevaCalificacion);

        // Agregar mensaje de éxito
        redirectAttributes.addFlashAttribute("mensaje", "¡Calificación guardada con éxito!");

        return "redirect:/cliente/historialPedido"; // Cambia según tu flujo
    }

    private Cliente obtenerClienteDeSesion(HttpSession session) {
        return (Cliente) session.getAttribute("cliente");
    }

    @GetMapping("/nuevo")
    public String mostrarFormCalificacion(@RequestParam Long id, Model model, HttpSession session) {

        Cliente cliente = (Cliente) session.getAttribute("cliente");
        model.addAttribute("idPe", id);
        model.addAttribute("idCli", cliente.getIdCliente());

        return "formulario/cliente/portal/calificacion";
    }

}
