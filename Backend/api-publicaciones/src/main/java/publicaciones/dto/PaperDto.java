package publicaciones.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperDto {

    @NotBlank(message = "El título es campo obligatorio")
    private String titulo;

    @Min(value = 1990, message = "El año debe ser mayor a 1990")
    @Max(value = 2035, message = "El año debe ser menor al 2035")
    private int anioPublicacion;

    @NotBlank(message = "La editorial es un campo obligatorio")
    private String editorial;

    private String isbn;

    @NotBlank(message = "El resumen es campo obligatorio")
    private String resumen;

    @NotBlank(message = "El área de investigación es campo obligatorio")
    private String areaInvestigacion;

    @NotBlank(message = "La revista es campo obligatorio")
    private String revista;

    private String indexacion;

    @Pattern(regexp = "^10\\.\\d{4,}/.+", message = "DOI debe tener formato válido (10.xxxx/xxxx)")
    private String doi;

    private String volumen;
    
    private String numero;
    
    @Min(value = 1, message = "La página de inicio debe ser mayor a 0")
    private Integer paginaInicio;
    
    @Min(value = 1, message = "La página final debe ser mayor a 0")
    private Integer paginaFin;
    
    private String palabrasClave;
    
    @NotBlank(message = "El abstract en inglés es obligatorio")
    private String abstractIngles;

    @NotNull(message = "Se requiere el ID del autor")
    private Long autorId;
}
