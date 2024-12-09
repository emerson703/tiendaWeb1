package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Marca;
import com.backus.proyecto.entity.transporte;
import com.backus.proyecto.services.MarcaService;
import com.backus.proyecto.services.TransporteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RequestMapping("/transporte")
@Controller
public class TransporteController {
    static String RUTA_VISTA = "/formulario/trabajador/transporte/";

    @Autowired
    private TransporteService transporteService;

    @GetMapping("/listar")
    public String listarTransp(Model model) {
        model.addAttribute("titulo", "Lista de Transportes disponibles para los repartidores");
        model.addAttribute("listTrans", transporteService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return RUTA_VISTA + "index";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Integer id, Model model) {
        transporte trans = transporteService.buscarPorId(id).orElse(null);
        if (trans != null) {
            model.addAttribute("transporte", trans);
            model.addAttribute("titulo", "Modificar Transporte");
            return RUTA_VISTA + "guardar";
        } else {
            model.addAttribute("error", "Marca no encontrada");
            return "redirect:/transporte/";
        }
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {

        model.addAttribute("transporte", new transporte());
        model.addAttribute("titulo", "Nuevo Transporte");
        return RUTA_VISTA + "guardar";
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarTransporte(@RequestBody transporte trans) {
        transporteService.guardar(trans);
        return ResponseEntity.ok("Registro grabado exitosamente");
    }


    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarTrans(@RequestParam("id") Integer id) {
        try {
            transporteService.eliminarPorId(id);
            return ResponseEntity.ok("Transporte eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar el transporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el transporte");
        }
    }
}
