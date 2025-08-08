package publicaciones.dto;

import lombok.Data;
import publicaciones.enums.TipoEvento;

import java.time.LocalDateTime;

/**
 * DTO para eventos de dominio
 */
@Data
public class EventoDominioDto {
    private Long id;
    private Long agregadoId;
    private String tipoAgregado;
    private TipoEvento tipoEvento;
    private LocalDateTime fechaOcurrencia;
    private String payload;
    private String usuarioId;
    private String correlationId;
}
