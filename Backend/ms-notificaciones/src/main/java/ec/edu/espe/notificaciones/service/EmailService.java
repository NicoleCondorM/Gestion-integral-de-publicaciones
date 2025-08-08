package ec.edu.espe.notificaciones.service;

import ec.edu.espe.notificaciones.domain.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    public boolean enviarEmail(Notificacion notificacion) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notificacion.getDestinatario());
            message.setSubject(notificacion.getAsunto());
            message.setText(notificacion.getMensaje());
            message.setFrom("noreply@editorial.com");
            
            mailSender.send(message);
            
            notificacion.setEstado(Notificacion.EstadoNotificacion.ENVIADO);
            notificacion.setFechaEnvio(LocalDateTime.now());
            
            logger.info("Email enviado exitosamente a: {}", notificacion.getDestinatario());
            return true;
            
        } catch (Exception e) {
            logger.error("Error enviando email a {}: {}", notificacion.getDestinatario(), e.getMessage());
            notificacion.setEstado(Notificacion.EstadoNotificacion.ERROR);
            notificacion.setErrorMessage(e.getMessage());
            return false;
        }
    }
}
