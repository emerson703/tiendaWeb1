package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Categoria;
import com.backus.proyecto.services.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RequestMapping("/categoria")
@Controller
public class CategoriaController {
static String RUTA_VISTA="/formulario/trabajador/categoria/";
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/")
    public String listarCategorias(Model model) {
        model.addAttribute("titulo", "Lista de Categorías");
        model.addAttribute("listCategoria", categoriaService.listar());
        // Otros atributos
        return RUTA_VISTA + "index";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Integer id, Model model) {
        Categoria categoria = categoriaService.buscarPorId(id).get();
        if (categoria != null) {
            model.addAttribute("categoria", categoria);
            model.addAttribute("titulo", "Modificar Categoría");
        }
        return RUTA_VISTA + "guardar";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Categoria categoria = new Categoria();
        categoria.setVisible(true);
        model.addAttribute("categoria", categoria);
        model.addAttribute("titulo", "Nueva Categoría");
        return RUTA_VISTA + "guardar";
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarMarca(@RequestBody Categoria categoria) {
        categoriaService.guardar(categoria);
        return ResponseEntity.ok("Registro grabado exitosamente");
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarCategoria(@RequestParam("id") Integer id) {
        try {
            categoriaService.eliminarPorId(id);
            return ResponseEntity.ok("Categoría eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la categoría");
        }
    }



}