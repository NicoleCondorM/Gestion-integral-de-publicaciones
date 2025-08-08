package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import publicaciones.enums.TipoPublicacion;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "articulos")
@Setter
@Getter
public class Paper extends Publicacion {

    private String areaInvestigacion;
    
    @Column(name = "nombre_revista")
    private String revista;
    
    @Column(name = "seccion_revista")
    private String seccion;
    
    @Column(name = "indexacion")
    private String indexacion;

    @Column(unique = true)
    private String doi;

    @Column(name = "volumen_revista")
    private String volumen;

    @Column(name = "numero_revista")
    private String numero;

    @Column(name = "pagina_inicio")
    private Integer paginaInicio;

    @Column(name = "pagina_fin")
    private Integer paginaFin;

    @Column(name = "abstract_ingles", columnDefinition = "TEXT")
    private String abstractIngles;

    // Referencias bibliográficas como JSON
    @Column(name = "referencias_bibliograficas", columnDefinition = "TEXT")
    @Convert(converter = ReferenciasBibliograficasConverter.class)
    private List<ReferenciaBibliografica> referenciasBibliograficas = new ArrayList<>();

    // Información sobre figuras y tablas
    @Column(name = "numero_figuras")
    private Integer numeroFiguras = 0;

    @Column(name = "numero_tablas")
    private Integer numeroTablas = 0;

    // Metadatos de figuras y tablas como JSON
    @Column(name = "metadata_figuras_tablas", columnDefinition = "TEXT")
    @Convert(converter = FigurasTablaMetadataConverter.class)
    private List<FiguraTablaMetadata> figurasTablasMetadata = new ArrayList<>();

    // Relación con autor principal (mantenida por compatibilidad)
    @ManyToOne
    @JoinColumn(name = "autor_id")
    @JsonIgnore
    private Autor autor;

    // Constructor
    public Paper() {
        super();
        this.setTipo(TipoPublicacion.ARTICULO);
    }

    // Método específico para artículos - cita completa
    public String getCitaCompleta() {
        StringBuilder cita = new StringBuilder();
        if (autor != null) {
            cita.append(autor.getNombre()).append(" ").append(autor.getApellido()).append(". ");
        }
        cita.append(getTitulo()).append(". ");
        if (revista != null) {
            cita.append(revista).append(". ");
        }
        if (volumen != null) {
            cita.append("Vol. ").append(volumen);
        }
        if (numero != null) {
            cita.append(", No. ").append(numero);
        }
        if (paginaInicio != null && paginaFin != null) {
            cita.append(", pp. ").append(paginaInicio).append("-").append(paginaFin);
        }
        cita.append(". ").append(getAnioPublicacion()).append(".");
        
        return cita.toString();
    }

    // Métodos para manejo de referencias bibliográficas
    public void agregarReferencia(ReferenciaBibliografica referencia) {
        if (referencia != null) {
            referenciasBibliograficas.add(referencia);
        }
    }

    public void removerReferencia(Integer numeroReferencia) {
        referenciasBibliograficas.removeIf(ref -> numeroReferencia.equals(ref.getNumero()));
    }

    public ReferenciaBibliografica obtenerReferencia(Integer numero) {
        return referenciasBibliograficas.stream()
                .filter(ref -> numero.equals(ref.getNumero()))
                .findFirst()
                .orElse(null);
    }

    public Integer getTotalReferencias() {
        return referenciasBibliograficas.size();
    }

    public Integer getTotalPaginasArticulo() {
        if (paginaInicio != null && paginaFin != null) {
            return paginaFin - paginaInicio + 1;
        }
        return null;
    }

    // Métodos para manejo de figuras y tablas
    public void agregarFiguraTablaMetadata(FiguraTablaMetadata metadata) {
        if (metadata != null) {
            figurasTablasMetadata.add(metadata);
        }
    }

    public Integer getTotalElementosVisuales() {
        return numeroFiguras + numeroTablas;
    }
}
