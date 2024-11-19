package com.backus.proyecto.controller;

import com.backus.proyecto.entity.Producto;
import com.backus.proyecto.services.CategoriaService;
import com.backus.proyecto.services.MarcaService;
import com.backus.proyecto.services.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.File;
import java.util.UUID;

@Slf4j
@RequestMapping("/producto")
@Controller
public class ProductoController {

    @Value("${ruta.almacenamiento}")
    private String rutaAlmacenamiento;


    static String RUTA_VISTA = "/formulario/trabajador/producto/";

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private MarcaService marcaService;

    @GetMapping("/")
    public String listarproductos(Model model) {
        model.addAttribute("titulo", "Lista de producto");
        model.addAttribute("listProducto", productoService.listar());
        model.addAttribute("mensajeList", "Sin datos para mostrar");
        return RUTA_VISTA + "index";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id).orElse(null);
        if (producto != null) {
            model.addAttribute("producto", producto);
            model.addAttribute("titulo", "Modificar producto");
            model.addAttribute("listCategorias", categoriaService.listar()); // Lista de categorías para el select
            model.addAttribute("listMarcas", marcaService.listar()); // Lista de marcas para el select
            return RUTA_VISTA + "guardar";
        } else {
            model.addAttribute("error", "producto no encontrada");
            return "redirect:/producto/";
        }
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
       Producto p= new Producto();
       p.setVisible(true);
        model.addAttribute("producto",p );
        model.addAttribute("titulo", "Nueva producto");
        model.addAttribute("listCategorias", categoriaService.listar()); // Lista de categorías para el select
        model.addAttribute("listMarcas", marcaService.listar()); // Lista de categorías para el select
        return RUTA_VISTA + "guardar";
    }





    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarproducto(@RequestParam("id") Integer id) {
        try {
            productoService.eliminarPorId(id);
            return ResponseEntity.ok("producto eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar la producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la producto");
        }
    }




    @GetMapping("/{nombreImagen:.+}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreImagen) {
        Path rutaArchivo = Paths.get(rutaAlmacenamiento + nombreImagen);

        // Verificar si el archivo solicitado existe
        if (!StringUtils.isEmpty(nombreImagen) && Files.exists(rutaArchivo)) {
            File file = rutaArchivo.toFile();
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok().body(resource);
        } else {
            // Si el archivo no existe, devolver la imagen por defecto
            Path rutaImagenPorDefecto = Paths.get(rutaAlmacenamiento + "default.jpg");
            File file = rutaImagenPorDefecto.toFile();
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok().body(resource);
        }
    }


    @PostMapping("/guardar")
    public ResponseEntity<String> guardarProducto(@ModelAttribute Producto producto,
                                                  @RequestParam(value = "imagenB", required = false) MultipartFile imagenB,
                                                  @RequestParam(value = "videoB", required = false) MultipartFile videoB) {
        try {
            // Verificar si el producto ya existe para actualizarlo correctamente
            if (producto.getIdProducto() != null) {
                Producto preUpdate = productoService.buscarPorId(producto.getIdProducto()).orElse(null);

                // Si se encontró el producto existente, mantener las imágenes actuales si no se suben nuevas
                if (preUpdate != null) {
                    if (imagenB == null || imagenB.isEmpty()) {
                        producto.setImagen(preUpdate.getImagen());
                    }

                }
            }

            // Guardar archivos si se han subido nuevos con nombres únicos
            if (imagenB != null && !imagenB.isEmpty()) {
                String nombreImagen = guardarArchivo(imagenB, rutaAlmacenamiento);
                producto.setImagen(nombreImagen); // Actualizar nombre de imagen en el producto
            }



            // Lógica para guardar el producto en la base de datos
            productoService.guardar(producto);

            return ResponseEntity.ok("Producto guardado exitosamente");
        } catch (IOException e) {
            // Manejar errores de IO al guardar archivos
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar archivos: " + e.getMessage());
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud: " + e.getMessage());
        }
    }


    private String guardarArchivo(MultipartFile archivo, String rutaAlmacenamiento) throws IOException {
        String nombreOriginal = archivo.getOriginalFilename();
        String nombreUnico = UUID.randomUUID().toString() + "_" + nombreOriginal; // Generar nombre único
        Path rutaArchivo = Paths.get(rutaAlmacenamiento + nombreUnico);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        return nombreUnico;
    }



}
