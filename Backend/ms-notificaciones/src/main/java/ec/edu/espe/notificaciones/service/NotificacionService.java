package ec.edu.espe.notificaciones.service;

import ec.edu.espe.notificaciones.domain.Notificacion;
import ec.edu.espe.notificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private WebSocketService webSocketService;

    public void procesarNotificacion(String destinatario, String asunto, String mensaje, 
                                   Notificacion.TipoNotificacion tipo) {
        
        Notificacion notificacion = new Notificacion(destinatario, asunto, mensaje, tipo);
        notificacion = notificacionRepository.save(notificacion);
        
        boolean enviado = false;
        
        switch (tipo) {
            case EMAIL:
                enviado = emailService.enviarEmail(notificacion);
                break;
            case WEBSOCKET:
                enviado = webSocketService.enviarNotificacionWebSocket(notificacion);
                break;
            case PUSH:
                // Implementar push notifications si es necesario
                logger.info("Push notifications no implementadas aún");
                break;
        }
        
        // Actualizar el estado en la base de datos
        notificacionRepository.save(notificacion);
        
        if (enviado) {
            logger.info("Notificación {} enviada exitosamente a {}", tipo, destinatario);
        } else {
            logger.error("Error enviando notificación {} a {}", tipo, destinatario);
        }
    }

    @Transactional(readOnly = true)
    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerPorDestinatario(String destinatario) {
        return notificacionRepository.findByDestinatario(destinatario);
    }
    
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerPorEstado(Notificacion.EstadoNotificacion estado) {
        return notificacionRepository.findByEstado(estado);
    }
    
    @Transactional(readOnly = true)
    public long contarPorEstado(Notificacion.EstadoNotificacion estado) {
        return notificacionRepository.countByEstado(estado);
    }
}
