package ec.edu.espe.auth.config;

import ec.edu.espe.auth.model.Role;
import ec.edu.espe.auth.model.User;
import ec.edu.espe.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Inicializando usuarios por defecto del sistema editorial...");
            
            // Administrador del sistema
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@editorial.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_EDITOR, Role.ROLE_LECTOR));
            userRepository.save(admin);
            
            // Editor - Decide aprobación final, fuerza estados especiales
            User editor = new User();
            editor.setUsername("editor");
            editor.setEmail("editor@editorial.com");
            editor.setPassword(passwordEncoder.encode("editor123"));
            editor.setRoles(Set.of(Role.ROLE_EDITOR, Role.ROLE_LECTOR));
            userRepository.save(editor);
            
            // Autor - Crea y actualiza borradores, responde solicitudes de cambio
            User autor = new User();
            autor.setUsername("autor");
            autor.setEmail("autor@editorial.com");
            autor.setPassword(passwordEncoder.encode("autor123"));
            autor.setRoles(Set.of(Role.ROLE_AUTOR, Role.ROLE_LECTOR));
            userRepository.save(autor);
            
            // Revisor - Evalúa, comenta y emite recomendaciones
            User revisor = new User();
            revisor.setUsername("revisor");
            revisor.setEmail("revisor@editorial.com");
            revisor.setPassword(passwordEncoder.encode("revisor123"));
            revisor.setRoles(Set.of(Role.ROLE_REVISOR, Role.ROLE_LECTOR));
            userRepository.save(revisor);
            
            // Lector - Accede al catálogo publicado y consulta metadatos
            User lector = new User();
            lector.setUsername("lector");
            lector.setEmail("lector@editorial.com");
            lector.setPassword(passwordEncoder.encode("lector123"));
            lector.setRoles(Set.of(Role.ROLE_LECTOR));
            userRepository.save(lector);
            
            // Usuario de prueba con múltiples roles
            User multirol = new User();
            multirol.setUsername("multirol");
            multirol.setEmail("multirol@editorial.com");
            multirol.setPassword(passwordEncoder.encode("multi123"));
            multirol.setRoles(Set.of(Role.ROLE_AUTOR, Role.ROLE_REVISOR, Role.ROLE_LECTOR));
            userRepository.save(multirol);
            
            log.info("Usuarios del sistema editorial inicializados correctamente:");
            log.info("Admin: username=admin, password=admin123 (ADMIN, EDITOR, LECTOR)");
            log.info("Editor: username=editor, password=editor123 (EDITOR, LECTOR)");
            log.info("Autor: username=autor, password=autor123 (AUTOR, LECTOR)");
            log.info("Revisor: username=revisor, password=revisor123 (REVISOR, LECTOR)");
            log.info("Lector: username=lector, password=lector123 (LECTOR)");
            log.info("Multirol: username=multirol, password=multi123 (AUTOR, REVISOR, LECTOR)");
        }
    }
}
