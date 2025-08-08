package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CambioRevision {
    
    private LocalDateTime fecha;
    private String descripcion;
    private String usuario;
    private String estadoAnterior;
    private String estadoNuevo;
    private String detalles;
}
