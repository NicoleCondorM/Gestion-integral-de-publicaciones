package publicaciones.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import publicaciones.dto.EventoDominioDto;
import publicaciones.enums.TipoEvento;
import publicaciones.model.EventoDominio;
import publicaciones.model.OutboxEvento;
import publicaciones.repository.EventoDominioRepository;
import publicaciones.repository.OutboxEventoRepository;
import publicaciones.service.OutboxEventProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Monitoreo de eventos de dominio y sistema Outbox")
public class EventoController {

    private final EventoDominioRepository eventoDominioRepository;
    private final OutboxEventoRepository outboxEventoRepository;
    private final OutboxEventProcessor outboxEventProcessor;

    @Operation(summary = "Listar eventos por publicación")
    @GetMapping("/publicacion/{publicacionId}")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<List<EventoDominioDto>> listarEventosPorPublicacion(
            @Parameter(description = "ID de la publicación") @PathVariable Long publicacionId) {
        List<EventoDominio> eventos = eventoDominioRepository.findByAgregadoIdOrderByFechaOcurrenciaDesc(publicacionId);
        List<EventoDominioDto> eventosDto = eventos.stream().map(this::convertirADto).collect(Collectors.toList());
        return ResponseEntity.ok(eventosDto);
    }

    @Operation(summary = "Listar eventos por tipo")
    @GetMapping("/tipo/{tipoEvento}")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<List<EventoDominioDto>> listarEventosPorTipo(
            @Parameter(description = "Tipo de evento") @PathVariable TipoEvento tipoEvento) {
        List<EventoDominio> eventos = eventoDominioRepository.findByTipoEventoOrderByFechaOcurrenciaDesc(tipoEvento);
        List<EventoDominioDto> eventosDto = eventos.stream().map(this::convertirADto).collect(Collectors.toList());
        return ResponseEntity.ok(eventosDto);
    }

    @Operation(summary = "Listar eventos por usuario")
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<List<EventoDominioDto>> listarEventosPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable String usuarioId) {
        List<EventoDominio> eventos = eventoDominioRepository.findByUsuarioId(usuarioId);
        List<EventoDominioDto> eventosDto = eventos.stream().map(this::convertirADto).collect(Collectors.toList());
        return ResponseEntity.ok(eventosDto);
    }

    @Operation(summary = "Listar eventos en rango de fechas")
    @GetMapping("/rango")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<List<EventoDominioDto>> listarEventosEnRango(
            @Parameter(description = "Fecha desde") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fecha hasta") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        List<EventoDominio> eventos = eventoDominioRepository.findEventosEnRango(desde, hasta);
        List<EventoDominioDto> eventosDto = eventos.stream().map(this::convertirADto).collect(Collectors.toList());
        return ResponseEntity.ok(eventosDto);
    }

    // ===== MONITOREO DEL SISTEMA OUTBOX =====

    @Operation(summary = "Listar eventos pendientes en Outbox")
    @GetMapping("/outbox/pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OutboxEvento>> listarEventosPendientes() {
        List<OutboxEvento> eventosPendientes = outboxEventoRepository.findEventosPendientesParaProcesar();
        return ResponseEntity.ok(eventosPendientes);
    }

    @Operation(summary = "Listar eventos fallidos en Outbox")
    @GetMapping("/outbox/fallidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OutboxEvento>> listarEventosFallidos() {
        List<OutboxEvento> eventosFallidos = outboxEventoRepository.findEventosFallidos();
        return ResponseEntity.ok(eventosFallidos);
    }

    @Operation(summary = "Forzar procesamiento de evento específico")
    @PostMapping("/outbox/{eventoId}/reprocesar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reprocesarEvento(
            @Parameter(description = "ID del evento Outbox") @PathVariable Long eventoId) {
        try {
            outboxEventProcessor.reprocesarEvento(eventoId);
            return ResponseEntity.ok("Evento reprocesado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al reprocesar evento: " + e.getMessage());
        }
    }

    @Operation(summary = "Estadísticas del sistema de eventos")
    @GetMapping("/estadisticas")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<EstadisticasEventosDto> obtenerEstadisticas() {
        long totalEventos = eventoDominioRepository.count();
        long eventosPendientes = outboxEventoRepository.findEventosPendientesParaProcesar().size();
        long eventosFallidos = outboxEventoRepository.findEventosFallidos().size();
        long eventosProcesados = outboxEventoRepository.count() - eventosPendientes - eventosFallidos;

        EstadisticasEventosDto estadisticas = new EstadisticasEventosDto();
        estadisticas.setTotalEventos(totalEventos);
        estadisticas.setEventosPendientes(eventosPendientes);
        estadisticas.setEventosFallidos(eventosFallidos);
        estadisticas.setEventosProcesados(eventosProcesados);

        return ResponseEntity.ok(estadisticas);
    }

    // ===== MÉTODOS AUXILIARES =====

    private EventoDominioDto convertirADto(EventoDominio evento) {
        EventoDominioDto dto = new EventoDominioDto();
        dto.setId(evento.getId());
        dto.setAgregadoId(evento.getAgregadoId());
        dto.setTipoAgregado(evento.getTipoAgregado());
        dto.setTipoEvento(evento.getTipoEvento());
        dto.setFechaOcurrencia(evento.getFechaOcurrencia());
        dto.setPayload(evento.getPayload());
        dto.setUsuarioId(evento.getUsuarioId());
        dto.setCorrelationId(evento.getCorrelationId());
        return dto;
    }

    // DTO interno para estadísticas
    public static class EstadisticasEventosDto {
        private long totalEventos;
        private long eventosPendientes;
        private long eventosFallidos;
        private long eventosProcesados;

        // Getters y setters
        public long getTotalEventos() { return totalEventos; }
        public void setTotalEventos(long totalEventos) { this.totalEventos = totalEventos; }
        
        public long getEventosPendientes() { return eventosPendientes; }
        public void setEventosPendientes(long eventosPendientes) { this.eventosPendientes = eventosPendientes; }
        
        public long getEventosFallidos() { return eventosFallidos; }
        public void setEventosFallidos(long eventosFallidos) { this.eventosFallidos = eventosFallidos; }
        
        public long getEventosProcesados() { return eventosProcesados; }
        public void setEventosProcesados(long eventosProcesados) { this.eventosProcesados = eventosProcesados; }
    }
}
