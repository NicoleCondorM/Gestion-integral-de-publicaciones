package ec.edu.espe.notificaciones.service;

import ec.edu.espe.notificaciones.domain.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public boolean enviarNotificacionWebSocket(Notificacion notificacion) {
        try {
            Map<String, Object> mensaje = new HashMap<>();
            mensaje.put("id", notificacion.getId());
            mensaje.put("asunto", notificacion.getAsunto());
            mensaje.put("mensaje", notificacion.getMensaje());
            mensaje.put("fecha", LocalDateTime.now().toString());
            mensaje.put("tipo", "NOTIFICACION");
            
            // Enviar a un usuario específico
            messagingTemplate.convertAndSendToUser(
                notificacion.getDestinatario(), 
                "/queue/notificaciones", 
                mensaje
            );
            
            // También enviar a un tópico general si es necesario
            messagingTemplate.convertAndSend("/topic/notificaciones", mensaje);
            
            notificacion.setEstado(Notificacion.EstadoNotificacion.ENVIADO);
            notificacion.setFechaEnvio(LocalDateTime.now());
            
            logger.info("Notificación WebSocket enviada a: {}", notificacion.getDestinatario());
            return true;
            
        } catch (Exception e) {
            logger.error("Error enviando notificación WebSocket a {}: {}", 
                        notificacion.getDestinatario(), e.getMessage());
            notificacion.setEstado(Notificacion.EstadoNotificacion.ERROR);
            notificacion.setErrorMessage(e.getMessage());
            return false;
        }
    }
}
