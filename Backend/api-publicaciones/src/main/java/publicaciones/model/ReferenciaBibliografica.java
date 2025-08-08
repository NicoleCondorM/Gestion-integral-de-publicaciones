package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenciaBibliografica {
    
    private Integer numero;
    private String autores;
    private String titulo;
    private String fuente; // Revista, libro, etc.
    private String volumen;
    private String numeroRevista;
    private String paginas;
    private Integer anio;
    private String doi;
    private String url;
    private String tipoReferencia; // ARTICULO, LIBRO, WEB, TESIS, etc.
    private String editorial;
    private String isbn;
    private String fechaAcceso; // Para referencias web
    
    public String generarCitaAPA() {
        StringBuilder cita = new StringBuilder();
        
        if (autores != null) {
            cita.append(autores).append(" ");
        }
        
        if (anio != null) {
            cita.append("(").append(anio).append("). ");
        }
        
        if (titulo != null) {
            cita.append(titulo).append(". ");
        }
        
        if (fuente != null) {
            cita.append(fuente);
            if (volumen != null) {
                cita.append(", ").append(volumen);
            }
            if (numeroRevista != null) {
                cita.append("(").append(numeroRevista).append(")");
            }
            if (paginas != null) {
                cita.append(", ").append(paginas);
            }
            cita.append(". ");
        }
        
        if (doi != null) {
            cita.append("https://doi.org/").append(doi);
        } else if (url != null) {
            cita.append(url);
        }
        
        return cita.toString().trim();
    }
}
