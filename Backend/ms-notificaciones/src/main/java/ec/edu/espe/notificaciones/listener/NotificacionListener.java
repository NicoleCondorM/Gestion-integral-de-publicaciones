package ec.edu.espe.notificaciones.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.notificaciones.domain.Notificacion;
import ec.edu.espe.notificaciones.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificacionListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionListener.class);

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "publicacion.publicada")
    public void manejarPublicacionPublicada(String mensajeJson) {
        try {
            logger.info("Evento recibido - Publicación publicada: {}", mensajeJson);
            
            JsonNode evento = objectMapper.readTree(mensajeJson);
            String eventoTipo = evento.get("eventType").asText();
            
            if ("PUBLICACION_PUBLICADA".equals(eventoTipo)) {
                JsonNode publicacion = evento.get("publicacion");
                String titulo = publicacion.get("titulo").asText();
                String autor = publicacion.get("autorPrincipal").asText();
                
                String asunto = "Nueva publicación disponible: " + titulo;
                String mensaje = String.format(
                    "Se ha publicado una nueva obra:\n\nTítulo: %s\nAutor: %s\n\n¡Ya disponible en nuestro catálogo!",
                    titulo, autor
                );
                
                // Enviar notificación por email y WebSocket
                notificacionService.procesarNotificacion(
                    "admin@editorial.com", asunto, mensaje, Notificacion.TipoNotificacion.EMAIL);
                
                notificacionService.procesarNotificacion(
                    "admin", asunto, mensaje, Notificacion.TipoNotificacion.WEBSOCKET);
            }
            
        } catch (Exception e) {
            logger.error("Error procesando evento de publicación: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "publicacion.cambios")
    public void manejarCambiosSolicitados(String mensajeJson) {
        try {
            logger.info("Evento recibido - Cambios solicitados: {}", mensajeJson);
            
            JsonNode evento = objectMapper.readTree(mensajeJson);
            String eventoTipo = evento.get("eventType").asText();
            
            if ("CAMBIOS_SOLICITADOS".equals(eventoTipo)) {
                String publicacionId = evento.get("publicacionId").asText();
                String autorEmail = evento.get("autorEmail").asText();
                String comentarios = evento.get("comentarios").asText();
                
                String asunto = "Cambios solicitados en su publicación";
                String mensaje = String.format(
                    "Se han solicitado cambios en su publicación (ID: %s):\n\nComentarios del revisor:\n%s\n\nPor favor, realice las modificaciones necesarias.",
                    publicacionId, comentarios
                );
                
                // Notificar al autor por email
                notificacionService.procesarNotificacion(
                    autorEmail, asunto, mensaje, Notificacion.TipoNotificacion.EMAIL);
            }
            
        } catch (Exception e) {
            logger.error("Error procesando evento de cambios: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "publicacion.aprobada")
    public void manejarPublicacionAprobada(String mensajeJson) {
        try {
            logger.info("Evento recibido - Publicación aprobada: {}", mensajeJson);
            
            JsonNode evento = objectMapper.readTree(mensajeJson);
            String eventoTipo = evento.get("eventType").asText();
            
            if ("PUBLICACION_APROBADA".equals(eventoTipo)) {
                String publicacionId = evento.get("publicacionId").asText();
                String autorEmail = evento.get("autorEmail").asText();
                String titulo = evento.get("titulo").asText();
                
                String asunto = "¡Su publicación ha sido aprobada!";
                String mensaje = String.format(
                    "Nos complace informarle que su publicación '%s' (ID: %s) ha sido aprobada y será publicada próximamente.\n\n¡Felicitaciones!",
                    titulo, publicacionId
                );
                
                // Notificar al autor
                notificacionService.procesarNotificacion(
                    autorEmail, asunto, mensaje, Notificacion.TipoNotificacion.EMAIL);
                
                notificacionService.procesarNotificacion(
                    autorEmail, asunto, mensaje, Notificacion.TipoNotificacion.WEBSOCKET);
            }
            
        } catch (Exception e) {
            logger.error("Error procesando evento de aprobación: {}", e.getMessage(), e);
        }
    }
}
