package publicaciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import publicaciones.enums.TipoEvento;

import java.time.LocalDateTime;

/**
 * Evento de dominio para publicaciones
 */
@Entity(name = "eventos_dominio")
@Setter
@Getter
@NoArgsConstructor
public class EventoDominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agregado_id", nullable = false)
    private Long agregadoId; // ID de la publicación

    @Column(name = "tipo_agregado", nullable = false)
    private String tipoAgregado = "Publicacion";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipoEvento;

    @Column(name = "fecha_ocurrencia", nullable = false)
    private LocalDateTime fechaOcurrencia = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String payload; // JSON con datos del evento

    @Column(name = "usuario_id")
    private String usuarioId;

    @Column(name = "correlation_id")
    private String correlationId;

    // Constructor para crear eventos
    public EventoDominio(Long agregadoId, TipoEvento tipoEvento, String payload, String usuarioId) {
        this.agregadoId = agregadoId;
        this.tipoEvento = tipoEvento;
        this.payload = payload;
        this.usuarioId = usuarioId;
        this.fechaOcurrencia = LocalDateTime.now();
    }

    public static EventoDominio crear(Long publicacionId, TipoEvento tipoEvento, String payload, String usuarioId) {
        return new EventoDominio(publicacionId, tipoEvento, payload, usuarioId);
    }
}
