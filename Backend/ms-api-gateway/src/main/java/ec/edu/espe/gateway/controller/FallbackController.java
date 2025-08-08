package ec.edu.espe.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    
    @GetMapping("/catalogo")
    public ResponseEntity<Map<String, Object>> catalogoFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Servicio de catálogo temporalmente no disponible");
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("timestamp", LocalDateTime.now());
        response.put("fallback", true);
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/publicaciones")
    public ResponseEntity<Map<String, Object>> publicacionesFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Servicio de publicaciones temporalmente no disponible");
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("timestamp", LocalDateTime.now());
        response.put("fallback", true);
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/notificaciones")
    public ResponseEntity<Map<String, Object>> notificacionesFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Servicio de notificaciones temporalmente no disponible");
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("timestamp", LocalDateTime.now());
        response.put("fallback", true);
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/general")
    public ResponseEntity<Map<String, Object>> generalFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Servicio temporalmente no disponible");
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("timestamp", LocalDateTime.now());
        response.put("fallback", true);
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
