package com.backus.proyecto.services;


import com.backus.proyecto.repository.CalificacionRepository;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Paragraph;
import com.backus.proyecto.entity.Producto;
import jakarta.servlet.ServletOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
@Service
public class ReporteCalificacionService {
    @Autowired
    private CalificacionRepository calificacionRepository;

    public ReporteCalificacionService(CalificacionRepository calificacionRepository) {
        this.calificacionRepository = calificacionRepository;
    }
    public void generarReporteComentariosPorCalificacion(ServletOutputStream outputStream) {
        // Obtener los datos de las calificaciones
        List<Object[]> calificaciones = calificacionRepository.obtenerReporteComentariosPorCalificacion();

        try {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36); // Márgenes personalizados
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // Logo de la empresa Backus
            InputStream logoStream = getClass().getResourceAsStream("/static/assets/img/backus.png");
            Image logo = Image.getInstance(ImageIO.read(logoStream), null);
            logo.setAlignment(Element.ALIGN_CENTER);
            logo.scaleToFit(100, 100);
            document.add(logo);
            document.add(new Paragraph(" "));

            // Título del reporte
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Reporte de Satisfacción de los CLientes", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Agregar la tabla
            PdfPTable table = new PdfPTable(3); // 3 columnas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Estilo de la tabla
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("Calificación", headerFont));
            header1.setBackgroundColor(BaseColor.DARK_GRAY);
            header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            PdfPCell header2 = new PdfPCell(new Phrase("Cantidad de Calificaciones", headerFont));
            header2.setBackgroundColor(BaseColor.DARK_GRAY);
            header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            PdfPCell header3 = new PdfPCell(new Phrase("Comentarios Agrupados", headerFont));
            header3.setBackgroundColor(BaseColor.DARK_GRAY);
            header3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(header1);
            table.addCell(header2);
            table.addCell(header3);

            // Agregar los datos de la calificación
            for (Object[] fila : calificaciones) {
                PdfPCell cell1 = new PdfPCell(new Phrase(fila[0].toString()));
                cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Phrase(fila[1].toString()));
                cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell2);

                PdfPCell cell3 = new PdfPCell(new Phrase(fila[2].toString()));
                cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell3);
            }

            document.add(table);
            document.add(new Paragraph(" "));




            // Generar el gráfico de barras
            JFreeChart chart = createBarChart(calificaciones);
            BufferedImage chartImage = chart.createBufferedImage(500, 300); // Tamaño de la imagen del gráfico
            Image chartPdfImage = Image.getInstance(writer, chartImage, 1.0f);
            document.add(chartPdfImage);

            // Agregar leyenda explicativa sobre las calificaciones
            Font legendFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
            Paragraph legend = new Paragraph("Leyenda de Calificaciones:", legendFont);
            legend.setSpacingBefore(10f);
            document.add(legend);

            Paragraph legendDetails = new Paragraph("1 - Muy Malo.\n" +
                    "2 - Malo.\n" +
                    "3 - Regular.\n" +
                    "4 - Bueno.\n" +
                    "5 - Excelente.", legendFont);
            document.add(legendDetails);
            document.add(new Paragraph(" "));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para crear el gráfico de barras
    private JFreeChart createBarChart(List<Object[]> calificaciones) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Llenar el dataset con las calificaciones
        for (Object[] fila : calificaciones) {
            String calificacion = fila[0].toString();
            int cantidad = Integer.parseInt(fila[1].toString());
            dataset.addValue(cantidad, "Cantidad", calificacion);
        }

        // Crear el gráfico de barras
        return ChartFactory.createBarChart(
                "Calificaciones de Pedidos",    // Título del gráfico
                "valor de la Calificación",                 // Eje X
                "Cantidad de calificaciones",                     // Eje Y
                dataset,                        // Dataset
                PlotOrientation.VERTICAL,       // Orientación
                true,                           // Mostrar leyenda
                true,                           // Mostrar herramienta de información
                false                           // Mostrar URL
        );
    }

}
