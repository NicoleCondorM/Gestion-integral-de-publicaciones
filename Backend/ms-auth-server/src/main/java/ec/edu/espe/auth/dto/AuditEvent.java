package ec.edu.espe.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditEvent {
    private String eventType; // LOGIN, LOGOUT, REGISTER, TOKEN_REFRESH
    private String username;
    private String email;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
    private boolean success;
    private String errorMessage;
    private String additionalInfo;
    
    public static AuditEvent loginEvent(String username, String email, String ipAddress, 
                                       String userAgent, boolean success, String errorMessage) {
        return new AuditEvent("LOGIN", username, email, ipAddress, userAgent, 
                             LocalDateTime.now(), success, errorMessage, null);
    }
    
    public static AuditEvent registerEvent(String username, String email, String ipAddress, 
                                          String userAgent, boolean success, String errorMessage) {
        return new AuditEvent("REGISTER", username, email, ipAddress, userAgent, 
                             LocalDateTime.now(), success, errorMessage, null);
    }
    
    public static AuditEvent logoutEvent(String username, String ipAddress) {
        return new AuditEvent("LOGOUT", username, null, ipAddress, null, 
                             LocalDateTime.now(), true, null, null);
    }
    
    public static AuditEvent tokenRefreshEvent(String username) {
        return new AuditEvent("TOKEN_REFRESH", username, null, null, null, 
                             LocalDateTime.now(), true, null, null);
    }
}
