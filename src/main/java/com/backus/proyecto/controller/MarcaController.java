package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Marca;
import com.backus.proyecto.services.MarcaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/marca")
@Controller
public class MarcaController {
    static String RUTA_VISTA = "/formulario/trabajador/marca/";

    @Autowired
    private MarcaService marcaService;

    @GetMapping("/")
    public String listarMarcas(Model model) {
        model.addAttribute("titulo", "Lista de Marca");
        model.addAttribute("listMarca", marcaService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return RUTA_VISTA + "index";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Integer id, Model model) {
        Marca marca = marcaService.buscarPorId(id).orElse(null);
        if (marca != null) {
            model.addAttribute("marca", marca);
            model.addAttribute("titulo", "Modificar Marca");
            return RUTA_VISTA + "guardar";
        } else {
            model.addAttribute("error", "Marca no encontrada");
            return "redirect:/marca/";
        }
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {

        model.addAttribute("marca", new Marca());
        model.addAttribute("titulo", "Nueva Marca");
        return RUTA_VISTA + "guardar";
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarMarca(@RequestBody Marca marca) {
        marcaService.guardar(marca);
        return ResponseEntity.ok("Registro grabado exitosamente");
    }


    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarMarca(@RequestParam("id") Integer id) {
        try {
            marcaService.eliminarPorId(id);
            return ResponseEntity.ok("Marca eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar la marca", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la marca");
        }
    }
}
