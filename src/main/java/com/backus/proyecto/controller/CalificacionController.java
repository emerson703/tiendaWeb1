package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Calificacion;
import com.backus.proyecto.entity.Cliente;
import com.backus.proyecto.entity.Empleado;
import com.backus.proyecto.entity.Pedido;
import com.backus.proyecto.repository.CalificacionRepository;
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

    @GetMapping("/listar")
    public String listarCalificaciones(Model model) {
        model.addAttribute("titulo", "Lista de Calificaciones por parte de los clientes");
        model.addAttribute("listCalificacion", calificacionService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return "/formulario/trabajador/calificacion/index"; //
    }
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarCalificacion(@RequestBody Calificacion calificacion) {
        calificacionService.guardar(calificacion);
        return ResponseEntity.ok("Registro grabado exitosamente");
    }
    private Cliente obtenerClienteDeSesion(HttpSession session) {
        return (Cliente) session.getAttribute("cliente");
    }

    @GetMapping("/nuevo")
    public String mostrarFormCalificacion(@RequestParam Long id, Model model, HttpSession session) {
       /* if (obtenerClienteDeSesion(session) == null) {
            return "redirect:/cliente/login";
        }
        Calificacion calificacion = calificacionRepository.findById((id))
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));

        model.addAttribute("titulo", "Detalle de la calificacion");
        model.addAttribute("calificacion", calificacion);
        //model.addAttribute("listPedidoDetalle", calificacionService.buscarPorId((id))); */
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        model.addAttribute("idPe", id);
        model.addAttribute("idCli", cliente.getIdCliente());
        return "formulario/cliente/portal/calificacion";
    }
    @GetMapping("/new")
    public String formularioNuevo(Model model) {

        return "formulario/cliente/portal/calificacion";
    }
}
