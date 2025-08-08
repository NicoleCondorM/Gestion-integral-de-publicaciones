package ec.edu.espe.auth.controller;

import ec.edu.espe.auth.dto.LoginRequest;
import ec.edu.espe.auth.dto.RegisterRequest;
import ec.edu.espe.auth.dto.TokenResponse;
import ec.edu.espe.auth.model.Role;
import ec.edu.espe.auth.model.User;
import ec.edu.espe.auth.repository.UserRepository;
import ec.edu.espe.auth.service.AuditService;
import ec.edu.espe.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditService auditService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, 
                                  HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElse(null);
            
            String accessToken = jwtService.generateToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername());
            
            TokenResponse tokenResponse = new TokenResponse(
                    accessToken,
                    refreshToken,
                    86400000L, // 24 hours
                    loginRequest.getUsername()
            );
            
            // Auditar login exitoso
            auditService.auditLogin(loginRequest.getUsername(), 
                                  user != null ? user.getEmail() : null, 
                                  ipAddress, userAgent, true, null);
            
            return ResponseEntity.ok(tokenResponse);
            
        } catch (AuthenticationException e) {
            // Auditar login fallido
            auditService.auditLogin(loginRequest.getUsername(), null, 
                                  ipAddress, userAgent, false, e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest,
                                    HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        try {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                auditService.auditRegister(registerRequest.getUsername(), 
                                         registerRequest.getEmail(), 
                                         ipAddress, userAgent, false, "Username ya existe");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El username ya existe"));
            }
            
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                auditService.auditRegister(registerRequest.getUsername(), 
                                         registerRequest.getEmail(), 
                                         ipAddress, userAgent, false, "Email ya existe");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El email ya existe"));
            }
            
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            
            // Asignar roles por defecto si no se especifican
            Set<Role> roles = registerRequest.getRoles();
            if (roles == null || roles.isEmpty()) {
                roles = Set.of(Role.ROLE_LECTOR); // Rol por defecto
            }
            user.setRoles(roles);
            
            userRepository.save(user);
            
            // Auditar registro exitoso
            auditService.auditRegister(registerRequest.getUsername(), 
                                     registerRequest.getEmail(), 
                                     ipAddress, userAgent, true, null);
            
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario registrado exitosamente",
                    "roles", roles.toString()
            ));
            
        } catch (Exception e) {
            auditService.auditRegister(registerRequest.getUsername(), 
                                     registerRequest.getEmail(), 
                                     ipAddress, userAgent, false, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        try {
            if (jwtService.validateToken(refreshToken)) {
                String username = jwtService.extractUsername(refreshToken);
                
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
                String newAccessToken = jwtService.generateToken(username, 
                        Map.of("authorities", user.getAuthorities()));
                
                TokenResponse tokenResponse = new TokenResponse(
                        newAccessToken,
                        refreshToken,
                        86400000L,
                        username
                );
                
                // Auditar refresh de token
                auditService.auditTokenRefresh(username);
                
                return ResponseEntity.ok(tokenResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Refresh token inválido"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Error al refrescar el token"));
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        
        try {
            if (jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                User user = userRepository.findByUsername(username).orElse(null);
                
                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "username", username,
                        "roles", user != null ? user.getRoles() : Set.of()
                ));
            } else {
                return ResponseEntity.ok(Map.of("valid", false));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request,
                                   HttpServletRequest httpRequest) {
        String username = request.get("username");
        String ipAddress = getClientIpAddress(httpRequest);
        
        if (username != null) {
            auditService.auditLogout(username, ipAddress);
        }
        
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }
    
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(Map.of(
                "roles", Role.values(),
                "descriptions", Map.of(
                        "ROLE_AUTOR", "Crea y actualiza borradores, responde solicitudes de cambio",
                        "ROLE_REVISOR", "Evalúa, comenta y emite recomendaciones (aceptar, solicitar cambios, rechazar)",
                        "ROLE_EDITOR", "Decide aprobación final, fuerza estados especiales",
                        "ROLE_ADMIN", "Administrador del sistema con todos los permisos",
                        "ROLE_LECTOR", "Accede al catálogo publicado y consulta metadatos"
                )
        ));
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
