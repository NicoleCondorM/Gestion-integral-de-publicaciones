package publicaciones.dto;

import lombok.Data;
import publicaciones.enums.EstadoPublicacion;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de publicaciones con información del ciclo de vida
 */
@Data
public class PublicacionResponseDto {
    private String id;
    private String titulo;
    private int anioPublicacion;
    private String editorial;
    private String isbn;
    private String resumen;
    
    // Información del ciclo de vida
    private EstadoPublicacion estado;
    private String version;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime fechaEnvioRevision;
    private LocalDateTime fechaAprobacion;
    private LocalDateTime fechaPublicacion;
    
    // Información de autoría
    private String autorPrincipalId;
    private List<String> coAutoresIds;
    private List<String> palabrasClave;
    
    // Información del autor
    private String autorNombre;
    private String autorApellido;
    private String autorEmail;
    
    // Tipo de publicación
    private String tipoPublicacion;
    
    // Campos específicos de libros
    private Integer numeroPaginas;
    private String idioma;
    private Double precio;
    private String formato;
    private Integer numeroEdicion;
    private String genero;
    
    // Campos específicos de papers
    private String revista;
    private String indexacion;
    private String areaInvestigacion;
}
