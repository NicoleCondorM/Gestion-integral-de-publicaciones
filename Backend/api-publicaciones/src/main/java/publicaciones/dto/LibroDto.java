package publicaciones.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDto {

    @NotBlank(message = "El título es campo obligatorio")
    private String titulo;

    @Min(value = 1990, message = "El año debe ser mayor a 1990")
    @Max(value = 2035, message = "El año debe ser menor al 2035")
    private int anioPublicacion;

    @NotBlank(message = "La editorial es un campo obligatorio")
    private String editorial;

    @NotBlank(message = "El ISBN es campo obligatorio")
    private String isbn;

    @NotBlank(message = "El resumen es campo obligatorio")
    private String resumen;

    @NotBlank(message = "El genero es campo obligatorio")
    private String genero;

    @Positive(message = "El número de páginas debe ser positivo")
    private int numeroPaginas;

    @NotNull(message = "Se requiere el ID del autor")
    private Long autorId;

    // Nuevos campos para funcionalidades extendidas
    private String idioma;
    
    @DecimalMin(value = "0.0", message = "El precio debe ser positivo")
    private Double precio;
    
    private String formato; // Físico, Digital, Ambos
    
    private String palabrasClave;
    
    private String tablaContenidos;
    
    private Integer numeroEdicion;
}
