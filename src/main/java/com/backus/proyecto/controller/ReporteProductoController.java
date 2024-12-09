package com.backus.proyecto.controller;
import com.backus.proyecto.services.ReporteProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Controller;

@Controller
public class ReporteProductoController {
    @Autowired
    private ReporteProductosService reporteService;

    @GetMapping("/reporte/productos")
    @ResponseBody
    public void generarReporteProductosMasVendidos(HttpServletResponse response) throws IOException {
        // Configuramos la respuesta para enviar el archivo PDF al navegador
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=productos_mas_vendidos.pdf");

        // Obtenemos el flujo de salida para escribir el PDF
        try (ServletOutputStream out = response.getOutputStream()) {
            // Llamamos al servicio para generar el reporte
            reporteService.generarReporteProductosMasVendidosParaDescarga(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

