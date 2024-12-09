package com.backus.proyecto.controller;

import com.backus.proyecto.services.ReporteCalificacionService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller

public class ReporteCalificacionController {

    @Autowired
    private ReporteCalificacionService reporteCalificacionService;
    @GetMapping("/reporte/satisfaccion")
    public void generarReporteClientesMasVendidos(HttpServletResponse response) throws IOException {
        // Configurar la respuesta HTTP para que descargue el archivo PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=calificacion.pdf");

        // Obtener el flujo de salida de la respuesta
        ServletOutputStream outputStream = response.getOutputStream();

        // Llamar al servicio para generar el reporte de productos más vendidos
        reporteCalificacionService.generarReporteComentariosPorCalificacion(outputStream);

        // Cerrar el flujo de salida después de que el archivo haya sido enviado
        outputStream.close();
    }


}
