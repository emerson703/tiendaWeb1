package com.backus.proyecto.services;
import com.backus.proyecto.entity.Pedido;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
@Service
public class ReportePedidoService {
    public ByteArrayOutputStream generarReportePedido(List<Pedido> pedidos) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Agregar el logo
        InputStream logoStream = getClass().getResourceAsStream("/static/assets/img/backus.png");
        if (logoStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo: /static/assets/img/backus.png");
        }
        try {
            byte[] logoBytes = logoStream.readAllBytes();
            Image logo = Image.getInstance(logoBytes);
            logo.setAlignment(Element.ALIGN_CENTER);
            logo.scaleToFit(100, 100); // Ajusta el tamaño del logo
            document.add(logo);
            document.add(new Paragraph(" ")); // Espacio después del logo
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar el logo: " + e.getMessage(), e);
        }
        // Título
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("Reporte de Pedidos Pendientes y Entregados", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Tabla
        PdfPTable table = new PdfPTable(7); // Número de columnas
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2, 1, 3, 2, 2, 2});
        table.setSpacingBefore(10);

        // Encabezados
        Font fontEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        String[] headers = {"ID", "Fecha", "Cant", "Cliente", "Método de Pago", "Estado", "Repartidor"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fontEncabezado));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Rellenar datos
        for (Pedido pedido : pedidos) {
            table.addCell(crearCeldaCentrada(pedido.getIdPedido().toString()));
            table.addCell(crearCeldaCentrada(pedido.getFechaOperacion().toString()));
            table.addCell(crearCeldaCentrada(String.valueOf(pedido.getCantidadP())));

            // Cliente o Anónimo
            String clienteInfo = (pedido.getCliente() != null)
                    ? pedido.getCliente().getNombre()
                    : pedido.getAnonimoNombre();
            table.addCell(crearCeldaCentrada(clienteInfo));

            table.addCell(crearCeldaCentrada(pedido.getMetodoPago()));
            table.addCell(crearCeldaCentrada(pedido.getEstado()));
            String repartidorInfo = (pedido.getRepartidor() != null)
                    ? pedido.getRepartidor().getNombre()
                    : "No asignado";
            table.addCell(crearCeldaCentrada(repartidorInfo));
        }

        document.add(table);

        // Totales (opcional)
        document.add(new Paragraph("\nTotal de pedidos: " + pedidos.size(), FontFactory.getFont(FontFactory.HELVETICA, 12)));

        document.close();

        return out;
    }

    // Método para crear celdas con formato centrado
    private PdfPCell crearCeldaCentrada(String texto) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }
}
