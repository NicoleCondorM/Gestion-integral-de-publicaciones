package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadatosPublicacion {
    
    // Metadatos comunes
    private String isbn;
    private String doi;
    private Integer paginas;
    private String categoria;
    private String licencia;
    private String idioma;
    private String urlAccesoLibre;
    private List<String> tags;
    private Map<String, Object> propiedadesCustom;
    
    // Metadatos específicos para libros
    private String genero;
    private String formato; // Físico, Digital, Ambos
    private Double precio;
    private String dimensiones;
    private Double peso;
    private String tipoEncuadernacion;
    
    // Metadatos específicos para artículos
    private String tipoArticulo; // Investigación, Revisión, Comunicación breve
    private String metodologia;
    private List<String> financiamiento;
    private String conflictoIntereses;
    private String disponibilidadDatos;
    
    // Metadatos de indexación
    private List<String> basesIndexacion;
    private String factorImpacto;
    private Integer citaciones;
    
    // Metadatos adicionales
    private Map<String, String> identificadoresAlternativos; // ISSN, eISSN, etc.
    private String embargo; // Fecha de embargo si aplica
    private List<String> materialesSupplementarios;
}
