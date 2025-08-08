package ec.edu.espe.observability.service;

import ec.edu.espe.observability.service.PublicacionesServiceClient;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MonitoringService {
    
    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    @Autowired
    private PublicacionesServiceClient publicacionesClient;
    
    private final AtomicInteger healthCheckCounter = new AtomicInteger(0);
    private final Map<String, Object> lastHealthCheck = new HashMap<>();
    private final Map<String, Object> activeAlerts = new HashMap<>();
    
    @Scheduled(fixedRate = 30000) // Cada 30 segundos
    public void performHealthChecks() {
        logger.info("Realizando health checks programados...");
        
        healthCheckCounter.incrementAndGet();
        meterRegistry.counter("health.checks.total").increment();
        
        // Check Publicaciones Service
        try {
            Object health = publicacionesClient.getHealth();
            lastHealthCheck.put("publicaciones", Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "response", health
            ));
            meterRegistry.counter("health.checks.success", "service", "publicaciones").increment();
        } catch (Exception e) {
            logger.error("Health check failed for publicaciones service: {}", e.getMessage());
            lastHealthCheck.put("publicaciones", Map.of(
                "status", "DOWN",
                "timestamp", LocalDateTime.now(),
                "error", e.getMessage()
            ));
            meterRegistry.counter("health.checks.failure", "service", "publicaciones").increment();
            
            // Crear alerta
            activeAlerts.put("publicaciones_down", Map.of(
                "service", "publicaciones",
                "level", "CRITICAL",
                "message", "Publicaciones service is down",
                "timestamp", LocalDateTime.now()
            ));
        }
    }
    
    public Map<String, Object> generateDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("timestamp", LocalDateTime.now());
        dashboard.put("healthChecksPerformed", healthCheckCounter.get());
        dashboard.put("servicesStatus", lastHealthCheck);
        dashboard.put("activeAlerts", activeAlerts.size());
        dashboard.put("systemInfo", Map.of(
            "totalMemory", Runtime.getRuntime().totalMemory(),
            "freeMemory", Runtime.getRuntime().freeMemory(),
            "maxMemory", Runtime.getRuntime().maxMemory(),
            "availableProcessors", Runtime.getRuntime().availableProcessors()
        ));
        
        return dashboard;
    }
    
    public Map<String, Object> checkAllServicesHealth() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("lastCheck", LocalDateTime.now());
        healthStatus.put("services", lastHealthCheck);
        
        // Calcular estado general
        boolean allUp = lastHealthCheck.values().stream()
            .allMatch(service -> {
                if (service instanceof Map) {
                    return "UP".equals(((Map<?, ?>) service).get("status"));
                }
                return false;
            });
        
        healthStatus.put("overallStatus", allUp ? "UP" : "DEGRADED");
        
        return healthStatus;
    }
    
    public Map<String, Object> getMetricsSummary() {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("timestamp", LocalDateTime.now());
        metrics.put("healthChecks", Map.of(
            "total", healthCheckCounter.get(),
            "lastRun", LocalDateTime.now()
        ));
        
        // Métricas del sistema
        metrics.put("system", Map.of(
            "uptime", System.currentTimeMillis(),
            "memory", Map.of(
                "used", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
                "free", Runtime.getRuntime().freeMemory(),
                "total", Runtime.getRuntime().totalMemory()
            )
        ));
        
        return metrics;
    }
    
    public Map<String, Object> getActiveAlerts() {
        Map<String, Object> alerts = new HashMap<>();
        alerts.put("timestamp", LocalDateTime.now());
        alerts.put("count", activeAlerts.size());
        alerts.put("alerts", activeAlerts);
        
        return alerts;
    }
    
    public void clearAlert(String alertId) {
        activeAlerts.remove(alertId);
        logger.info("Alert cleared: {}", alertId);
    }
}
