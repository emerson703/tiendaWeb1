package com.backus.proyecto.services;
import com.backus.proyecto.repository.EntregaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletOutputStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jfree.chart.encoders.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service

public class ReporteEmpleadoService {
    @Autowired
    private EntregaRepository entregaRepository;

    public void generarReporteDesempeño(ServletOutputStream outputStream) {
        try {
            // Obtener los datos
            List<Object[]> desempeñoEmpleados = entregaRepository.obtenerDesempeñoEmpleados();

            // Crear el documento PDF
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Reporte de Desempeño de Repartidores", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Tabla de datos
            PdfPTable table = new PdfPTable(4); // 4 columnas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{3, 3, 2, 3});

            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

            // Cabeceras
            String[] headers = {"Nombre del Repartidor", "Apellidos del Repartidor", "Total de Pedidos Entregados", "Última Entrega"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
                cell.setBackgroundColor(BaseColor.GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Estilo para datos
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 11);

            for (Object[] fila : desempeñoEmpleados) {
                for (Object value : fila) {
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", dataFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    table.addCell(cell);
                }
            }

            document.add(table);

            // Crear el gráfico circular
            DefaultPieDataset dataset = new DefaultPieDataset();
            for (Object[] fila : desempeñoEmpleados) {
                String nombreCompleto = fila[0] + " " + fila[1];
                int totalPedidos = Integer.parseInt(fila[2].toString());
                dataset.setValue(nombreCompleto, totalPedidos);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Distribución de Pedidos Entregados por Repartidor", // Título
                    dataset, // Datos
                    true, // Leyenda
                    true, // Tooltips
                    false // URLs
            );

            // Convertir el gráfico en una imagen PNG
            BufferedImage chartImage = chart.createBufferedImage(500, 400);
            ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", chartOut);
            byte[] chartBytes = chartOut.toByteArray();

            // Incluir el gráfico en el PDF
            Image chartPdf = Image.getInstance(chartBytes);
            chartPdf.setAlignment(Image.ALIGN_CENTER);
            chartPdf.scaleToFit(400, 300);
            document.add(new Paragraph(" "));
            document.add(chartPdf);

            // Cerrar el documento
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
