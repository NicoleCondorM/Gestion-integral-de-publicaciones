package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Capitulo {
    
    private Integer numero;
    private String titulo;
    private String resumenCapitulo;
    private Integer paginaInicio;
    private Integer paginaFin;
    private List<String> autoresCapitulo; // Para capítulos con autores específicos
    private List<String> palabrasClave;
    private String contenidoResumen; // Resumen más detallado si es necesario
    
    public Integer getNumeroPaginas() {
        if (paginaInicio != null && paginaFin != null) {
            return paginaFin - paginaInicio + 1;
        }
        return null;
    }
}
