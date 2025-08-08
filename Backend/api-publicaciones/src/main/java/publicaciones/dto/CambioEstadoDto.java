package publicaciones.dto;

import lombok.Data;
import publicaciones.enums.EstadoPublicacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para cambios de estado en el ciclo de vida de publicaciones
 */
@Data
public class CambioEstadoDto {
    
    @NotNull(message = "El nuevo estado es requerido")
    private EstadoPublicacion nuevoEstado;
    
    @NotBlank(message = "El usuario es requerido")
    private String usuarioId;
    
    private String comentarios;
    
    private String revisorAsignado;
    
    private String motivo; // Para el caso de retirar publicación
}
