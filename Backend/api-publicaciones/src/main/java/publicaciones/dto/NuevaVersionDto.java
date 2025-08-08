package publicaciones.dto;

import lombok.Data;

/**
 * DTO para crear nuevas versiones de publicaciones
 */
@Data
public class NuevaVersionDto {
    private String comentarios;
    private String creadoPor;
    private Long publicacionOriginalId;
    
    // Datos de la nueva versión
    private String titulo;
    private String resumen;
    private String editorial;
    private String isbn;
    
    // Campos específicos según el tipo
    private String genero; // Para libros
    private Integer numeroPaginas; // Para libros
    private String areaInvestigacion; // Para artículos
    private String revista; // Para artículos
    private String doi; // Para artículos
}
