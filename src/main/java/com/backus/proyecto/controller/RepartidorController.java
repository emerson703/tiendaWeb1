package com.backus.proyecto.controller;

import com.backus.proyecto.entity.*;
import com.backus.proyecto.repository.*;
import com.backus.proyecto.services.BoletaPDFService;
import com.backus.proyecto.services.EmailServicePDF;
import com.backus.proyecto.services.PedidoService;
import com.backus.proyecto.services.RepartidorService;
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

@RequestMapping("/repartidor")
@Controller
public class RepartidorController {
    @Autowired
    private RepartidorRepository repartidorRepository;
    @Autowired
    private RepartidorService repartidorService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private EntregaRepository entregaRepository;
    @Autowired
    private DetalleRepository detalleRepository;
    @Autowired
    private BoletaPDFService boletaPDFService;
    @Autowired
    private EmailServicePDF emailServicePDF;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @GetMapping("/listar")
    public String listarRepartidor(Model model) {
        model.addAttribute("titulo", "Lista de Repartidores");
        model.addAttribute("listRepartidor", repartidorService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return "/formulario/trabajador/repartidor/index"; //
    }
    @GetMapping("/listarAsignacion")
    public String listarAsignacion(Model model, HttpSession session) {
        Repartidor repartidor = obtenerRepartidorDeSesion(session);

        if (repartidor == null) {
            model.addAttribute("error", "Debe iniciar sesión para ver sus asignaciones.");
            return "redirect:/empleado/login";
        }

        List<Pedido> pedidosAsignados = pedidoRepository.findByRepartidorIdAndEstado(repartidor.getId(), "PENDIENTE");
        model.addAttribute("titulo", "Lista de pedidos asignados para UD:");
        model.addAttribute("listaPedido", pedidosAsignados);
        model.addAttribute("mensajeList", pedidosAsignados.isEmpty() ? "Sin datos para mostrar" : "");

        return "/formulario/repartidor/portalRepartidor/asignaciones";
    }
    @GetMapping("/listarEntregados")
    public String listarEntregados(Model model, HttpSession session) {
        Repartidor repartidor = obtenerRepartidorDeSesion(session);

        if (repartidor == null) {
            model.addAttribute("error", "Debe iniciar sesión para ver sus asignaciones.");
            return "redirect:/empleado/login";
        }

        List<Pedido> pedidosAsignados = pedidoRepository.findByRepartidorIdAndEstado(repartidor.getId(), "ENTREGADO");
        model.addAttribute("titulo", "Lista de pedidos entregados por UD:");
        model.addAttribute("listaPedido", pedidosAsignados);
        model.addAttribute("mensajeList", pedidosAsignados.isEmpty() ? "Sin datos para mostrar" : "");

        return "/formulario/repartidor/portalRepartidor/entregados";
    }
    @PostMapping("/realizarEntrega")
    public String realizarEntrega(@RequestParam Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        // Obtener el objeto "empleado" desde la sesión, que puede ser un Repartidor o un Empleado
        Object empleadoObj = session.getAttribute("repartidor");  // Aquí puede ser tanto un Empleado o Repartidor
        if (empleadoObj == null) {
            redirectAttributes.addFlashAttribute("error", "Empleado no encontrado en la sesión.");
            return "redirect:/repartidor/logueo";
        }

        try {
            Pedido pedido = pedidoRepository.findById(Math.toIntExact(id))
                    .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));


            Repartidor repartidor = obtenerRepartidorDeSesion(session);
            Entrega entrega = new Entrega(null, LocalDateTime.now(), null, pedido, repartidor);
            entregaRepository.save(entrega);

            // Cambiar el estado del pedido
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

        return "redirect:/repartidor/listarAsignacion";
    }


    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Repartidor repartidor = new Repartidor();
        repartidor.setVisible(true);
        model.addAttribute("repartidor", repartidor);
        model.addAttribute("titulo", "Nuevo repartidor");
        return "/formulario/trabajador/repartidor/guardar";
    }
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarRepartidor(@RequestBody Repartidor repartidor) {
        repartidorService.guardar(repartidor);
        return ResponseEntity.ok("Registro grabado exitosamente");
    }
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Long id, Model model) {
        Repartidor repartidor = repartidorService.buscarPorId(id).orElse(null);
        if (repartidor != null) {
            model.addAttribute("repartidor", repartidor);
            model.addAttribute("titulo", "Modificar repartidor");
            return "/formulario/trabajador/repartidor/guardar";
        } else {
            model.addAttribute("error", "repartidor no encontrada");
            return "redirect:/repartidor/index";
        }
    }
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarRepartidor(@RequestParam("id") Long id) {
        try {
            repartidorService.eliminarPorId(id);
            return ResponseEntity.ok("repartidor eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el empleado");
        }
    }
    //FIN DE CRUD
    //LOGUEO
    @GetMapping("/logueo")
    public String mostrarFormularioLogin() {
        return "formulario/repartidor/portalRepartidor/login";
    }
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String correo, @RequestParam String clave, Model model, HttpSession session) {
        Repartidor repartidor = repartidorRepository.findByCorreo(correo);

        if (repartidor != null && repartidor.getClave().equals(clave)) {
            session.setAttribute("repartidor", repartidor);
            return "redirect:/repartidor/datos";
        } else {
            model.addAttribute("error", "Correo o clave incorrectos");
            return "formulario/repartidor/portalRepartidor/login";
        }
    }
    private Repartidor obtenerRepartidorDeSesion(HttpSession session) {
        return (Repartidor) session.getAttribute("repartidor");
    }
    @GetMapping("/datos")
    public String mostrarDatos(Model model, HttpSession session) {
        Repartidor repartidor = obtenerRepartidorDeSesion(session);
        if (repartidor != null) {
            model.addAttribute("repartidor", repartidor);
            return "formulario/repartidor/portalRepartidor/inicio";
        } else {
            return "redirect:/empleado/login";
        }
    }
    //PERFIL
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, HttpSession session) {
        Repartidor repartidor = obtenerRepartidorDeSesion(session); // Lógica para obtener el cliente actual
        model.addAttribute("repartidor", repartidorRepository.findByCorreo(repartidor.getCorreo()));
        return "formulario/repartidor/portalRepartidor/perfil";
    }
    @GetMapping("/editarPerfil")
    public String editarPerfil(Model model, HttpSession session) {
        Repartidor repartidor = obtenerRepartidorDeSesion(session);// Lógica para obtener el cliente actual
        model.addAttribute("repartidor", repartidorRepository.findByCorreo(repartidor.getCorreo()));
        return "formulario/repartidor/portalRepartidor/editarPerfil";
    }
    @PostMapping("/editPerfil")
    public String guardarCambios(@ModelAttribute("repartidor") Repartidor repartidor, Model model, HttpSession session) {
        // Lógica para guardar los cambios en el perfil
        Repartidor repartidorx = obtenerRepartidorDeSesion(session);// Lógica para obtener el cliente actual
        repartidor.setCorreo(repartidorx.getCorreo());
        repartidor.setVisible(repartidorx.isVisible());
        repartidorRepository.save(repartidor);
        model.addAttribute("mensaje", "Perfil actualizado exitosamente");
        return "formulario/repartidor/portalRepartidor/perfil";
    }
    //mapas - rutas - ubicacion
    @GetMapping("/ruta")
    public String mapas(@RequestParam("id") Integer idPedido, Model model) {
        // Busca el pedido basado en el ID
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Obtén el cliente asociado al pedido
        Cliente cliente = pedido.getCliente();

        if (cliente != null) {
            // Añade las coordenadas del cliente al modelo
            model.addAttribute("latitud", cliente.getLatitud());
            model.addAttribute("longitud", cliente.getLongitud());
        } else {
            model.addAttribute("mensajeList", "No se encontraron coordenadas para el cliente.");
        }

        // Añade el título de la página
        model.addAttribute("titulo", "Trazar ruta de Backus hasta el Cliente");
        return "/formulario/repartidor/rutas/index"; // Redirige a la página del mapa
    }
}
