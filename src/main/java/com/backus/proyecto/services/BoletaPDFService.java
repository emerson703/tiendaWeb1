package com.backus.proyecto.services;
import com.backus.proyecto.entity.Pedido;
import com.backus.proyecto.entity.Detalle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class BoletaPDFService {
    public byte[] generarBoletaPDF(Pedido pedido, List<Detalle> detalles) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Título
        document.add(new Paragraph("Boleta de Venta Electrónica"));
        document.add(new Paragraph("Número de Pedido: " + pedido.getIdPedido()));
        document.add(new Paragraph("Fecha: " + pedido.getFechaOperacion()));
        document.add(new Paragraph("Cliente: " + (pedido.getCliente() != null ? pedido.getCliente().getNombre() : pedido.getAnonimoNombre())));

        // Tabla de Detalles del Pedido
        PdfPTable table = new PdfPTable(4);
        table.addCell("Producto");
        table.addCell("Cantidad");
        table.addCell("Precio Unitario");
        table.addCell("Subtotal");

        for (Detalle detalle : detalles) {
            table.addCell(detalle.getProducto().getNombre());
            table.addCell(String.valueOf(detalle.getCantidad()));
            table.addCell(String.valueOf(detalle.getPrecio()));
            table.addCell(String.valueOf(detalle.getSubtotal()));
        }
        document.add(table);

        // Total
        document.add(new Paragraph("Total: " + pedido.getCantidadP())); // Puedes ajustar según el cálculo de total

        document.close();
        return out.toByteArray();
    }
}
