package publicaciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import publicaciones.enums.TipoEvento;

import java.time.LocalDateTime;

/**
 * Tabla Outbox para patrón transaccional de eventos
 * Garantiza que los eventos se publiquen de manera consistente
 */
@Entity(name = "outbox_eventos")
@Setter
@Getter
@NoArgsConstructor
public class OutboxEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evento_id", nullable = false)
    private String eventoId; // UUID único del evento

    @Column(name = "agregado_id", nullable = false)
    private Long agregadoId;

    @Column(name = "tipo_agregado", nullable = false)
    private String tipoAgregado = "Publicacion";

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento", nullable = false)
    private TipoEvento tipoEvento;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_procesado")
    private LocalDateTime fechaProcesado;

    @Column(nullable = false)
    private Boolean procesado = false;

    @Column(name = "intentos")
    private Integer intentos = 0;

    @Column(name = "ultimo_error", columnDefinition = "TEXT")
    private String ultimoError;

    @Column(name = "routing_key")
    private String routingKey;

    @Column(name = "exchange_name")
    private String exchangeName;

    // Constructor para crear outbox eventos
    public OutboxEvento(String eventoId, Long agregadoId, TipoEvento tipoEvento, 
                       String payload, String routingKey, String exchangeName) {
        this.eventoId = eventoId;
        this.agregadoId = agregadoId;
        this.tipoEvento = tipoEvento;
        this.payload = payload;
        this.routingKey = routingKey;
        this.exchangeName = exchangeName;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Métodos de negocio
    public void marcarComoProcesado() {
        this.procesado = true;
        this.fechaProcesado = LocalDateTime.now();
    }

    public void incrementarIntentos(String error) {
        this.intentos++;
        this.ultimoError = error;
    }

    public boolean debeReintentarse() {
        return !procesado && intentos < 5;
    }
}
