package com.backus.proyecto.services;
import com.backus.proyecto.entity.Cliente;
import com.backus.proyecto.repository.DetalleRepository;
import com.backus.proyecto.repository.PedidoRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Paragraph;
import com.backus.proyecto.entity.Producto;
import jakarta.servlet.ServletOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
@Service
public class ReporteProductosService {
    @Autowired
    private DetalleRepository detalleRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    // Método para generar el reporte de productos más vendidos (archivo PDF)
    public void generarReporteProductosMasVendidosParaDescarga(ServletOutputStream outputStream) {
        // Obtener los productos más vendidos
        List<Object[]> productosMasVendidos = detalleRepository.findMostSoldProducts();

        try {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);  // Márgenes personalizados
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            InputStream logoStream = getClass().getResourceAsStream("/static/assets/img/backus.png");

            Image logo = Image.getInstance(ImageIO.read(logoStream), null);
            logo.setAlignment(Element.ALIGN_CENTER);
            logo.scaleToFit(100, 100);
            document.add(logo);
            document.add(new Paragraph(" "));

            // Título del reporte
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Reporte de Productos Más Vendidos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Tabla de productos más vendidos
            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[]{3, 2});
            table.setWidthPercentage(100);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

            PdfPCell header1 = new PdfPCell(new Phrase("Producto", headerFont));
            header1.setBackgroundColor(BaseColor.DARK_GRAY);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header1);

            PdfPCell header2 = new PdfPCell(new Phrase("Cantidad Vendida en uds.", headerFont));
            header2.setBackgroundColor(BaseColor.DARK_GRAY);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header2);

            Font rowFont = new Font(Font.FontFamily.HELVETICA, 12);
            for (Object[] result : productosMasVendidos) {
                Producto producto = (Producto) result[0];
                Long cantidadVendida = (Long) result[1];
                String nombreProducto = producto.getNombre() ;

                PdfPCell cell1 = new PdfPCell(new Phrase(nombreProducto, rowFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(cantidadVendida), rowFont));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell2);
            }

            // Agregar la tabla al documento
            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void generarReporteClientesMasFrecuentesParaDescarga(ServletOutputStream outputStream) {
        // Obtener los clientes más frecuentes
        List<Object[]> clientesMasFrecuentes = pedidoRepository.findMostFrequentClients();

        try {
            // Crear el documento PDF
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);  // Usamos el OutputStream en lugar de un archivo físico
            document.open();
            //LOGO
            InputStream logoStream = getClass().getResourceAsStream("/static/assets/img/backus.png");

            Image logo = Image.getInstance(ImageIO.read(logoStream), null);
            logo.setAlignment(Element.ALIGN_CENTER);
            logo.scaleToFit(100, 100);
            document.add(logo);
            document.add(new Paragraph(" "));
            // Título del reporte con estilo
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Reporte de Clientes Más Frecuentes", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));  // Espaciado

            // Crear la tabla con dos columnas: Cliente y Cantidad de Pedidos
            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[]{3, 2});  // Ajustar el ancho de las columnas
            table.setWidthPercentage(100);  // Ocupa todo el ancho de la página

            // Encabezados de la tabla con estilo
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("Cliente", headerFont));
            header1.setBackgroundColor(BaseColor.DARK_GRAY);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header1);

            PdfPCell header2 = new PdfPCell(new Phrase("Cantidad de Pedidos", headerFont));
            header2.setBackgroundColor(BaseColor.DARK_GRAY);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header2);

            // Llenar la tabla con los datos
            Font rowFont = new Font(Font.FontFamily.HELVETICA, 12);
            for (Object[] result : clientesMasFrecuentes) {
                Cliente cliente = (Cliente) result[0];  // Cliente
                Long cantidadPedidos = (Long) result[1];  // Cantidad de pedidos

                // Obtener el nombre del cliente
                String nombreCliente = cliente.getNombre();

                // Agregar las celdas a la tabla
                PdfPCell cell1 = new PdfPCell(new Phrase(nombreCliente, rowFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(cantidadPedidos), rowFont));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell2);
            }

            // Agregar la tabla al documento PDF
            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
