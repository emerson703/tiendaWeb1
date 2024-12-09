package com.backus.proyecto.controller;
import com.backus.proyecto.services.ReporteProductosService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller

public class ReporteClienteController {
    @Autowired
    private ReporteProductosService reporteProductosService;

    // Endpoint para generar el reporte de productos más vendidos en PDF
    @GetMapping("/reporte/clientes")
    public void generarReporteClientesMasVendidos(HttpServletResponse response) throws IOException {
        // Configurar la respuesta HTTP para que descargue el archivo PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=productos_mas_vendidos.pdf");

        // Obtener el flujo de salida de la respuesta
        ServletOutputStream outputStream = response.getOutputStream();

        // Llamar al servicio para generar el reporte de productos más vendidos
        reporteProductosService.generarReporteClientesMasFrecuentesParaDescarga(outputStream);

        // Cerrar el flujo de salida después de que el archivo haya sido enviado
        outputStream.close();
    }
}
