package ec.edu.espe.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.auth.dto.AuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    
    private static final String AUDIT_EXCHANGE = "audit.exchange";
    private static final String AUDIT_ROUTING_KEY = "audit.auth";
    
    public void publishAuditEvent(AuditEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(AUDIT_EXCHANGE, AUDIT_ROUTING_KEY, eventJson);
            log.debug("Evento de auditoría publicado: {} para usuario: {}", 
                     event.getEventType(), event.getUsername());
        } catch (Exception e) {
            log.error("Error publicando evento de auditoría: {}", e.getMessage());
        }
    }
    
    public void auditLogin(String username, String email, String ipAddress, 
                          String userAgent, boolean success, String errorMessage) {
        AuditEvent event = AuditEvent.loginEvent(username, email, ipAddress, 
                                               userAgent, success, errorMessage);
        publishAuditEvent(event);
    }
    
    public void auditRegister(String username, String email, String ipAddress, 
                             String userAgent, boolean success, String errorMessage) {
        AuditEvent event = AuditEvent.registerEvent(username, email, ipAddress, 
                                                  userAgent, success, errorMessage);
        publishAuditEvent(event);
    }
    
    public void auditLogout(String username, String ipAddress) {
        AuditEvent event = AuditEvent.logoutEvent(username, ipAddress);
        publishAuditEvent(event);
    }
    
    public void auditTokenRefresh(String username) {
        AuditEvent event = AuditEvent.tokenRefreshEvent(username);
        publishAuditEvent(event);
    }
}
