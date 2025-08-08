package ec.edu.espe.observability.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "api-publicaciones", fallback = PublicacionesServiceFallback.class)
public interface PublicacionesServiceClient {
    
    @GetMapping("/actuator/health")
    Object getHealth();
    
    @GetMapping("/actuator/metrics")
    Object getMetrics();
}

@org.springframework.stereotype.Component
class PublicacionesServiceFallback implements PublicacionesServiceClient {
    
    @Override
    public Object getHealth() {
        return "Service Unavailable";
    }
    
    @Override
    public Object getMetrics() {
        return "Service Unavailable";
    }
}
