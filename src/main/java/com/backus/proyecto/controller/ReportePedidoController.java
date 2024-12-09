package com.backus.proyecto.controller;
import com.backus.proyecto.entity.Pedido;
import com.backus.proyecto.repository.PedidoRepository;
import com.backus.proyecto.services.ReportePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class ReportePedidoController {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ReportePedidoService reportePedidoService;

    @GetMapping("/reporte/pedidos")
    public ResponseEntity<byte[]> generarReportePedidos() {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll(); // Obtener todos los pedidos
            ByteArrayOutputStream pdfOutput = reportePedidoService.generarReportePedido(pedidos);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Pedidos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfOutput.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
