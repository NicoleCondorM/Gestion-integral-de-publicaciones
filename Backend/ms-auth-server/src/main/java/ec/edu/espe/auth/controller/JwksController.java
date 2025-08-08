package ec.edu.espe.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class JwksController {
    
    @Value("${jwt.secret}")
    private String secret;
    
    /**
     * Endpoint JWKS (JSON Web Key Set) para que otros servicios puedan validar tokens
     */
    @GetMapping("/jwks.json")
    public ResponseEntity<?> getJwks() {
        // En producción, esto debería usar claves RSA públicas/privadas
        // Para simplicidad, exponemos información sobre la clave simétrica
        String encodedSecret = Base64.getEncoder().encodeToString(secret.getBytes());
        
        return ResponseEntity.ok(Map.of(
                "keys", new Object[] {
                    Map.of(
                            "kty", "oct", // Key Type: Octet sequence (for HMAC)
                            "use", "sig", // Use: Signature
                            "alg", "HS256", // Algorithm
                            "kid", "editorial-key-1", // Key ID
                            "k", encodedSecret // Key value (base64url encoded)
                    )
                }
        ));
    }
    
    /**
     * Endpoint de introspección para validar tokens
     */
    @GetMapping("/token-info")
    public ResponseEntity<?> getTokenInfo() {
        return ResponseEntity.ok(Map.of(
                "issuer", "ms-auth-server",
                "jwks_uri", "http://localhost:8762/.well-known/jwks.json",
                "supported_algorithms", new String[] {"HS256"},
                "token_endpoint", "http://localhost:8762/auth/login",
                "introspection_endpoint", "http://localhost:8762/auth/validate"
        ));
    }
}
