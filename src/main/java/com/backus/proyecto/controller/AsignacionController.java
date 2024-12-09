package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Repartidor;
import com.backus.proyecto.entity.transporte;
import org.springframework.ui.Model;
import com.backus.proyecto.entity.Asignacion;
import com.backus.proyecto.repository.RepartidorRepository;
import com.backus.proyecto.repository.TransporteRepository;
import com.backus.proyecto.services.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/asignaciones")
public class AsignacionController {
    @Autowired
    private AsignacionService asignacionService;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @GetMapping("/creare")
    public String mostrarFormularioDeAsignacion(Model model) {
        model.addAttribute("transportes", transporteRepository.findAll());
        model.addAttribute("repartidores", repartidorRepository.findAll());
        model.addAttribute("asignacion", new Asignacion());
        return "formulario/trabajador/repartidor/asignacion";
    }

    @PostMapping("/crear")
    public String asignarTransporte(@ModelAttribute Asignacion asignacion, Model model) {
        transporte transporte1 = transporteRepository.findById(asignacion.getTransporte().getId()).orElse(null);
        Repartidor repartidor = repartidorRepository.findById(asignacion.getRepartidor().getId()).orElse(null);

        if (transporte1 != null && repartidor != null) {
            asignacionService.asignarTransporteARepartidor(transporte1, repartidor);
        }

        return "redirect:/asignaciones/listar";
    }

    @GetMapping("/listar")
    public String listarAsignaciones(Model model) {
        model.addAttribute("asignaciones", asignacionService.listarAsignaciones());
        return "formulario/trabajador/repartidor/listaAsignacion";
    }
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Integer id, Model model) {
        Asignacion asignacion = asignacionService.obtenerAsignacionPorId(id);
        model.addAttribute("asignacion", asignacion);
        model.addAttribute("transportes", transporteRepository.findAll());
        model.addAttribute("repartidores", repartidorRepository.findAll());
        return "formulario/trabajador/repartidor/asignacion";
    }
    @PostMapping("/editar/{id}")
    public String actualizarAsignacion(@PathVariable Integer id, @ModelAttribute Asignacion asignacion) {
        transporte transporte1 = transporteRepository.findById(asignacion.getTransporte().getId()).orElse(null);
        Repartidor repartidor = repartidorRepository.findById(asignacion.getRepartidor().getId()).orElse(null);

        if (transporte1 != null && repartidor != null) {
            asignacionService.actualizarAsignacion(id, transporte1, repartidor);
        }

        return "redirect:/asignaciones/listar";
    }
    @GetMapping("/eliminar/{id}")
    public String eliminarAsignacion(@PathVariable Integer id) {
        asignacionService.eliminarAsignacion(id);
        return "redirect:/asignaciones/listar";
    }
}
