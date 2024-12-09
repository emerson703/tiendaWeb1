package com.backus.proyecto.controller;

import com.backus.proyecto.services.ReporteEmpleadoService;
import com.backus.proyecto.services.ReporteProductosService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
@Controller
public class ReporteEmpleadoController {
    @Autowired
    private ReporteEmpleadoService reporteEmpleadoService;

    @GetMapping("/reporte/empleado")
    public void generarReporteClientesMasVendidos(HttpServletResponse response) throws IOException {
        // Configurar la respuesta HTTP para que descargue el archivo PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=desempeño_empleado.pdf");

        // Obtener el flujo de salida de la respuesta
        ServletOutputStream outputStream = response.getOutputStream();

        // Llamar al servicio para generar el reporte de productos más vendidos
        reporteEmpleadoService.generarReporteDesempeño(outputStream);

        // Cerrar el flujo de salida después de que el archivo haya sido enviado
        outputStream.close();
    }
}
