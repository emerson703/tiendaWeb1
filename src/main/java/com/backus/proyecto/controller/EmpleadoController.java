package com.backus.proyecto.controller;

import com.backus.proyecto.entity.*;
import com.backus.proyecto.repository.*;
import com.backus.proyecto.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@RequestMapping("/empleado")
@Controller
public class EmpleadoController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private RepartidorRepository repartidorRepository;

    @Autowired
    private DetalleService detalleService;

    @Autowired
    private BoletaPDFService boletaPDFService;

    @Autowired
    private EmailServicePDF emailServicePDF;

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private DetalleRepository detalleRepository;
    @Autowired
    private RepartidorService   repartidorService;

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/empleado/login";
    }
    /*CRUD DE EMPLEADOS*/

    @GetMapping("/listar")
    public String listarEmpleados(Model model) {
        model.addAttribute("titulo", "Lista de Empleados");
        model.addAttribute("listEmpleado", empleadoService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return "/formulario/trabajador/empleado/index"; //
    }
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Empleado empleado = new Empleado();
        empleado.setVisible(true);
        model.addAttribute("empleado", empleado);
        model.addAttribute("titulo", "Nuevo Empleado");
        return "/formulario/trabajador/empleado/guardar";
    }
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarEmpleado(@RequestBody Empleado empleado) {
        empleadoService.guardar(empleado);
        return ResponseEntity.ok("Registro grabado exitosamente");
    }
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Long id, Model model) {
        Empleado empleado = empleadoService.buscarPorId(id).orElse(null);
        if (empleado != null) {
            model.addAttribute("empleado", empleado);
            model.addAttribute("titulo", "Modificar Empleado");
            return "/formulario/trabajador/empleado/guardar";
        } else {
            model.addAttribute("error", "Marca no encontrada");
            return "redirect:/empleado/index";
        }
    }
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarEmpleado(@RequestParam("id") Long id) {
        try {
            empleadoService.eliminarPorId(id);
            return ResponseEntity.ok("empleado eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el empleado");
        }
    }

    /*FIN DEL CRUD*/
    //INICIAR SESSION
    private Empleado obtenerEmpleadoDeSesion(HttpSession session) {
        return (Empleado) session.getAttribute("empleado");
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String correo, @RequestParam String clave, Model model, HttpSession session) {
        Empleado empleado = empleadoRepository.findByCorreo(correo);

        if (empleado != null && empleado.getClave().equals(clave)) {
            session.setAttribute("empleado", empleado);
            return "redirect:/empleado/datos";
        } else {
            model.addAttribute("error", "Correo o clave incorrectos");
            return "formulario/trabajador/portal/login";
        }
    }

    @GetMapping("/datos")
    public String mostrarDatos(Model model, HttpSession session) {
        Empleado empleado = obtenerEmpleadoDeSesion(session);
        if (empleado != null) {
            model.addAttribute("empleado", empleado);
            return "formulario/trabajador/portal/datos";
        } else {
            return "redirect:/empleado/login";
        }
    }
    //PERFIL
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, HttpSession session) {
        Empleado empleado = obtenerEmpleadoDeSesion(session); // Lógica para obtener el cliente actual
        model.addAttribute("empleado", empleadoRepository.findByCorreo(empleado.getCorreo()));
        return "formulario/trabajador/empleado/perfil";
    }
    @GetMapping("/editarPerfil")
    public String editarPerfil(Model model, HttpSession session) {
        Empleado empleado = obtenerEmpleadoDeSesion(session);// Lógica para obtener el cliente actual
        model.addAttribute("empleado", empleadoRepository.findByCorreo(empleado.getCorreo()));
        return "formulario/trabajador/empleado/editarPerfil";
    }
    @PostMapping("/editPerfil")
    public String guardarCambios(@ModelAttribute("empleado") Empleado empleado, Model model, HttpSession session) {
        // Lógica para guardar los cambios en el perfil
        Empleado empleadox = obtenerEmpleadoDeSesion(session);// Lógica para obtener el cliente actual
        empleado.setCorreo(empleadox.getCorreo());
        empleado.setVisible(empleadox.isVisible());
        empleado.setRol(empleadox.getRol());
        empleadoRepository.save(empleado);
        model.addAttribute("mensaje", "Perfil actualizado exitosamente");
        return "formulario/trabajador/empleado/perfil";
    }
    //Editar Pedido
    @GetMapping("/edit/{id}")
    public String formularioEditarPedido(@PathVariable("id") Integer id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id).orElse(null);
        if (pedido != null) {
            model.addAttribute("pedido", pedido);
            model.addAttribute("titulo", "Modificar Pedido");
            return "/formulario/trabajador/pedidosTrabajador/editar";
        } else {
            model.addAttribute("error", "Pedido no encontrado");
            return "redirect:/empleado/index";
        }
    }
    @PostMapping("/editarPedido")
    public ResponseEntity<String> editarPedido(@RequestBody Pedido pedido) {
        pedidoService.guardar(pedido);
        return ResponseEntity.ok("Registro grabado exitosamente");
    }

    @GetMapping("/pedidosPendientes")
    public String mostrarPedidosPendientes(Model model, HttpSession session) {
        if (obtenerEmpleadoDeSesion(session) == null) {
            return "redirect:/empleado/login";
        }
        model.addAttribute("titulo", "Pedidos Por atender");
        model.addAttribute("listPedido", pedidoRepository.findByEstado("PENDIENTE"));
        model.addAttribute("listRepartidor", repartidorService.listar());
        return "formulario/trabajador/pedidosTrabajador/index";
    }
    @GetMapping("/asignar")
    public String AsignarRepartidor(@RequestParam Long id, @RequestParam Long idre, RedirectAttributes redirectAttributes, HttpSession session) {
        if (obtenerEmpleadoDeSesion(session) == null) {
            redirectAttributes.addFlashAttribute("error", "Empleado no encontrado en la sesión.");
            return "redirect:/empleado/login";
        }

        try {
            // Buscar el pedido por su ID
            Pedido pedido = pedidoRepository.findById(Math.toIntExact(id))
                    .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));

            // Buscar el repartidor por el ID recibido
            Repartidor repartidor = repartidorRepository.findById(idre)
                    .orElseThrow(() -> new NoSuchElementException("Repartidor no encontrado"));

            // Asignar el repartidor al pedido (solo actualizamos esta relación)
            pedido.setRepartidor(repartidor);

            // Guardar los cambios solo para el campo 'Repartidor'
            pedidoRepository.save(pedido);

            // Mensaje de éxito

            redirectAttributes.addFlashAttribute("mensaje", "Repartidor asignado con éxito.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al asignar el repartidor.");
        }

        return "redirect:/empleado/pedidosPendientes";
    }

    @GetMapping("/pedidoDetalle")
    public String mostrarPedidoDetalle(@RequestParam Long id, Model model, HttpSession session) {
        if (obtenerEmpleadoDeSesion(session) == null) {
            return "redirect:/empleado/login";
        }
        Pedido pedido = pedidoRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));

        model.addAttribute("titulo", "Detalle del Pedido");
        model.addAttribute("pedido", pedido);
        model.addAttribute("listPedidoDetalle", detalleService.buscarPorPedido(Math.toIntExact(id)));
        return "formulario/trabajador/pedidosTrabajador/pedidodetalle";
    }

    @PostMapping("/entregarPedido")
    public String entregarPedido(@RequestParam Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (obtenerEmpleadoDeSesion(session) == null) {
            redirectAttributes.addFlashAttribute("error", "Empleado no encontrado en la sesión.");
            return "redirect:/empleado/login";
        }

        try {
            Pedido pedido = pedidoRepository.findById(Math.toIntExact(id))
                    .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));

            Empleado empleado = obtenerEmpleadoDeSesion(session);
            Entrega entrega = new Entrega(null, LocalDateTime.now(), empleado, pedido, null);
            entregaRepository.save(entrega);

            // Cambiar el estado
            pedido.setEstado("ENTREGADO");
            pedidoRepository.save(pedido);


            // Generar la boleta de venta en PDF
            List<Detalle> detalles = detalleRepository.findByPedido_IdPedido(Long.valueOf(pedido.getIdPedido()));
            byte[] pdfBytes = boletaPDFService.generarBoletaPDF(pedido, detalles);

            // Enviar la boleta al cliente por correo
            emailServicePDF.enviarCorreoConBoleta(
                    pedido.getCliente().getEmail(),
                    "Su pedido ha sido entregado",
                    "Adjunto a este correo encontrará la boleta de venta electrónica.",
                    pdfBytes
            );

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "Pedido entregado con éxito.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al entregar el pedido.");
        }

        return "redirect:/empleado/pedidosPendientes";
    }
    private Repartidor obtenerRepartidorDeSesion(HttpSession session) {
        return (Repartidor) session.getAttribute("repartidor");
    }

    @GetMapping("/entregas")
    public String mostrarEntregas(Model model, HttpSession session) {
        if (obtenerEmpleadoDeSesion(session) == null) {
            return "redirect:/empleado/login";
        }
        model.addAttribute("titulo", "Entregas Realizadas");
        model.addAttribute("listEntrega", entregaRepository.findAll());
        return "formulario/trabajador/entregas/index";
    }
    @GetMapping("/detalleEntrega")
    public String mostrarDetalleEntrega(@RequestParam Long id, Model model, HttpSession session) {
        if (obtenerEmpleadoDeSesion(session) == null) {
            return "redirect:/empleado/login";
        }
        Entrega entrega = entregaRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada"));

        model.addAttribute("titulo", "Detalle de la Entrega");
        model.addAttribute("entrega", entrega);
        model.addAttribute("pedido", entrega.getPedido());
        model.addAttribute("listPedidoDetalle", detalleService.buscarPorPedido(entrega.getPedido().getIdPedido()));

        return "formulario/trabajador/entregas/detalleEntrega";
    }
}

