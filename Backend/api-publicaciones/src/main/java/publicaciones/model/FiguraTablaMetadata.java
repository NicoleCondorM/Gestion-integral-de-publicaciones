package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FiguraTablaMetadata {
    
    private Integer numero;
    private String tipo; // FIGURA, TABLA, GRAFICO, ESQUEMA
    private String titulo;
    private String descripcion;
    private String ubicacion; // página o sección donde aparece
    private String fuente; // si es de fuente externa
    private String formato; // PNG, JPG, SVG, etc.
    private String tamaño; // pequeña, mediana, completa
    private Boolean esColorida;
    private String leyenda;
    private String creditos;
}
