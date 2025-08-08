package ec.edu.espe.observability.controller;

import ec.edu.espe.observability.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {
    
    @Autowired
    private MonitoringService monitoringService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = monitoringService.generateDashboard();
        return ResponseEntity.ok(dashboard);
    }
    
    @GetMapping("/services/health")
    public ResponseEntity<Map<String, Object>> getServicesHealth() {
        Map<String, Object> healthStatus = monitoringService.checkAllServicesHealth();
        return ResponseEntity.ok(healthStatus);
    }
    
    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> getMetricsSummary() {
        Map<String, Object> metrics = monitoringService.getMetricsSummary();
        return ResponseEntity.ok(metrics);
    }
    
    @GetMapping("/alerts")
    public ResponseEntity<Map<String, Object>> getAlerts() {
        Map<String, Object> alerts = monitoringService.getActiveAlerts();
        return ResponseEntity.ok(alerts);
    }
}
