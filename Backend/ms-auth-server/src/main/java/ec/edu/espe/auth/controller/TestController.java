package ec.edu.espe.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    
    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Este es un endpoint público del sistema editorial",
                "timestamp", System.currentTimeMillis()
        ));
    }
    
    @GetMapping("/lector")
    @PreAuthorize("hasRole('ROLE_LECTOR')")
    public ResponseEntity<?> lectorEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint para lectores - acceso al catálogo publicado",
                "timestamp", System.currentTimeMillis(),
                "role", "LECTOR"
        ));
    }
    
    @GetMapping("/autor")
    @PreAuthorize("hasRole('ROLE_AUTOR')")
    public ResponseEntity<?> autorEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint para autores - crear y actualizar borradores",
                "timestamp", System.currentTimeMillis(),
                "role", "AUTOR"
        ));
    }
    
    @GetMapping("/revisor")
    @PreAuthorize("hasRole('ROLE_REVISOR')")
    public ResponseEntity<?> revisorEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint para revisores - evaluar y comentar publicaciones",
                "timestamp", System.currentTimeMillis(),
                "role", "REVISOR"
        ));
    }
    
    @GetMapping("/editor")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<?> editorEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint para editores - decisión final de aprobación",
                "timestamp", System.currentTimeMillis(),
                "role", "EDITOR"
        ));
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> adminEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint administrativo - gestión completa del sistema",
                "timestamp", System.currentTimeMillis(),
                "role", "ADMIN"
        ));
    }
    
    @GetMapping("/autor-revisor")
    @PreAuthorize("hasRole('ROLE_AUTOR') and hasRole('ROLE_REVISOR')")
    public ResponseEntity<?> autorRevisorEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint para usuarios con rol de autor Y revisor",
                "timestamp", System.currentTimeMillis(),
                "roles", "AUTOR + REVISOR"
        ));
    }
    
    @GetMapping("/workflow")
    @PreAuthorize("hasAnyRole('ROLE_AUTOR', 'ROLE_REVISOR', 'ROLE_EDITOR')")
    public ResponseEntity<?> workflowEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint del flujo editorial - acceso para participantes del workflow",
                "timestamp", System.currentTimeMillis(),
                "allowedRoles", "AUTOR, REVISOR, EDITOR"
        ));
    }
}
