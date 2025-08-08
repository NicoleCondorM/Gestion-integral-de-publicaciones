package ec.edu.espe.notificaciones.controller;

import ec.edu.espe.notificaciones.domain.Notificacion;
import ec.edu.espe.notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {
    
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> listarTodas(){
        List<Notificacion> notificaciones = notificacionService.obtenerTodas();
        return ResponseEntity.ok(notificaciones);
    }
    
    @GetMapping("/destinatario/{destinatario}")
    public ResponseEntity<List<Notificacion>> obtenerPorDestinatario(@PathVariable String destinatario){
        List<Notificacion> notificaciones = notificacionService.obtenerPorDestinatario(destinatario);
        return ResponseEntity.ok(notificaciones);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> obtenerPorEstado(@PathVariable String estado){
        try {
            Notificacion.EstadoNotificacion estadoEnum = Notificacion.EstadoNotificacion.valueOf(estado.toUpperCase());
            List<Notificacion> notificaciones = notificacionService.obtenerPorEstado(estadoEnum);
            return ResponseEntity.ok(notificaciones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/enviar")
    public ResponseEntity<Map<String, String>> enviarNotificacion(
            @RequestParam String destinatario, 
            @RequestParam String asunto,
            @RequestParam String mensaje,
            @RequestParam String tipo) {
        try {
            Notificacion.TipoNotificacion tipoEnum = Notificacion.TipoNotificacion.valueOf(tipo.toUpperCase());
            notificacionService.procesarNotificacion(destinatario, asunto, mensaje, tipoEnum);
            return ResponseEntity.ok(Map.of("message", "Notificación enviada exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tipo de notificación inválido"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al enviar notificación"));
        }
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(){
        Map<String, Object> estadisticas = Map.of(
            "total", notificacionService.obtenerTodas().size(),
            "enviadas", notificacionService.contarPorEstado(Notificacion.EstadoNotificacion.ENVIADO),
            "pendientes", notificacionService.contarPorEstado(Notificacion.EstadoNotificacion.PENDIENTE),
            "errores", notificacionService.contarPorEstado(Notificacion.EstadoNotificacion.ERROR)
        );
        return ResponseEntity.ok(estadisticas);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notificaciones Service is running");
    }
}
