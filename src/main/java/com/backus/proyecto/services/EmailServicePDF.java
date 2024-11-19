package com.backus.proyecto.services;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.stereotype.Service;

@Service
public class EmailServicePDF {
    private final JavaMailSender mailSender;

    public EmailServicePDF(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void enviarCorreoConBoleta(String destinatario, String asunto, String mensaje, byte[] boletaPdf) {
        try {
            // Crear el mensaje MIME
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Configuración del correo
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(mensaje);

            // Adjuntar el PDF
            if (boletaPdf != null && boletaPdf.length > 0) {
                helper.addAttachment("BoletaPedido.pdf", new ByteArrayDataSource(boletaPdf, "application/pdf"));
            } else {
                throw new IllegalArgumentException("El archivo PDF de la boleta está vacío o nulo.");
            }

            // Enviar el correo
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Manejar errores de envío de correo
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Manejar errores relacionados con parámetros inválidos
            System.err.println("Error en los parámetros del correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
