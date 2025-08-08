package publicaciones.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuración para habilitar tareas programadas (Jobs)
 * Necesario para el procesamiento del Outbox
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    // La configuración está habilitada con @EnableScheduling
    // Los métodos @Scheduled en OutboxEventProcessor se ejecutarán automáticamente
}
