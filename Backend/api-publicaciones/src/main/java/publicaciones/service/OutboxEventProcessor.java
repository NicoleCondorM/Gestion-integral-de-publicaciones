package publicaciones.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import publicaciones.model.OutboxEvento;
import publicaciones.repository.OutboxEventoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para procesar eventos del patrón Outbox
 * Garantiza la entrega confiable de eventos a RabbitMQ
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {

    private final OutboxEventoRepository outboxEventoRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Procesa eventos pendientes cada 10 segundos
     */
    @Scheduled(fixedDelay = 10000) // 10 segundos
    @Transactional
    public void procesarEventosPendientes() {
        List<OutboxEvento> eventosPendientes = outboxEventoRepository.findEventosPendientesParaProcesar();
        
        if (eventosPendientes.isEmpty()) {
            return;
        }

        log.info("Procesando {} eventos pendientes del outbox", eventosPendientes.size());

        for (OutboxEvento evento : eventosPendientes) {
            try {
                publicarEvento(evento);
                evento.marcarComoProcesado();
                outboxEventoRepository.save(evento);
                
                log.debug("Evento {} procesado exitosamente", evento.getEventoId());
                
            } catch (Exception e) {
                log.error("Error al procesar evento {}: {}", evento.getEventoId(), e.getMessage());
                evento.incrementarIntentos(e.getMessage());
                outboxEventoRepository.save(evento);
                
                if (!evento.debeReintentarse()) {
                    log.error("Evento {} ha fallado después de {} intentos y no será reintentado", 
                             evento.getEventoId(), evento.getIntentos());
                }
            }
        }
    }

    /**
     * Publica un evento individual a RabbitMQ
     */
    private void publicarEvento(OutboxEvento evento) {
        try {
            rabbitTemplate.convertAndSend(
                evento.getExchangeName(),
                evento.getRoutingKey(),
                evento.getPayload(),
                message -> {
                    message.getMessageProperties().setMessageId(evento.getEventoId());
                    message.getMessageProperties().setTimestamp(
                        java.sql.Timestamp.valueOf(evento.getFechaCreacion())
                    );
                    message.getMessageProperties().getHeaders().put("eventoId", evento.getEventoId());
                    message.getMessageProperties().getHeaders().put("tipoEvento", evento.getTipoEvento().name());
                    message.getMessageProperties().getHeaders().put("agregadoId", evento.getAgregadoId().toString());
                    return message;
                }
            );
            
            log.debug("Evento {} publicado a exchange {} con routing key {}", 
                     evento.getEventoId(), evento.getExchangeName(), evento.getRoutingKey());
                     
        } catch (Exception e) {
            log.error("Error al publicar evento {} a RabbitMQ: {}", evento.getEventoId(), e.getMessage());
            throw e;
        }
    }

    /**
     * Limpia eventos procesados antiguos cada hora
     */
    @Scheduled(fixedDelay = 3600000) // 1 hora
    @Transactional
    public void limpiarEventosProcesados() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(7); // Eventos mayores a 7 días
        List<OutboxEvento> eventosAntiguos = outboxEventoRepository.findEventosProcesadosAnterioresA(fechaLimite);
        
        if (!eventosAntiguos.isEmpty()) {
            log.info("Limpiando {} eventos procesados antiguos", eventosAntiguos.size());
            outboxEventoRepository.deleteAll(eventosAntiguos);
        }
    }

    /**
     * Reporta eventos fallidos cada 30 minutos
     */
    @Scheduled(fixedDelay = 1800000) // 30 minutos
    public void reportarEventosFallidos() {
        List<OutboxEvento> eventosFallidos = outboxEventoRepository.findEventosFallidos();
        
        if (!eventosFallidos.isEmpty()) {
            log.warn("Hay {} eventos fallidos que requieren atención manual:", eventosFallidos.size());
            eventosFallidos.forEach(evento -> 
                log.warn("Evento fallido: ID={}, TipoEvento={}, Intentos={}, UltimoError={}", 
                        evento.getEventoId(), evento.getTipoEvento(), evento.getIntentos(), evento.getUltimoError())
            );
        }
    }

    /**
     * Fuerza el procesamiento de un evento específico
     */
    @Transactional
    public void reprocesarEvento(Long eventoId) {
        OutboxEvento evento = outboxEventoRepository.findById(eventoId)
            .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado: " + eventoId));
            
        if (evento.getProcesado()) {
            log.warn("Intento de reprocesar evento ya procesado: {}", evento.getEventoId());
            return;
        }

        try {
            publicarEvento(evento);
            evento.marcarComoProcesado();
            outboxEventoRepository.save(evento);
            
            log.info("Evento {} reprocesado exitosamente", evento.getEventoId());
            
        } catch (Exception e) {
            log.error("Error al reprocesar evento {}: {}", evento.getEventoId(), e.getMessage());
            evento.incrementarIntentos(e.getMessage());
            outboxEventoRepository.save(evento);
            throw e;
        }
    }
}
