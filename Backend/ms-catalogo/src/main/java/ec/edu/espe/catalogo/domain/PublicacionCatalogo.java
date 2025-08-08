package ec.edu.espe.catalogo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "publicaciones_catalogo")
public class PublicacionCatalogo {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String resumen;
    
    @Column(nullable = false)
    private String tipo; // LIBRO, PAPER, REVISTA
    
    @Column(nullable = false)
    private String estado; // PUBLICADO, RETIRADO
    
    private String editorial;
    
    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @ElementCollection
    @CollectionTable(name = "publicacion_autores", 
                    joinColumns = @JoinColumn(name = "publicacion_id"))
    @Column(name = "autor")
    private Set<String> autores;
    
    @ElementCollection
    @CollectionTable(name = "publicacion_categorias", 
                    joinColumns = @JoinColumn(name = "publicacion_id"))
    @Column(name = "categoria")
    private Set<String> categorias;
    
    // Constructors
    public PublicacionCatalogo() {}
    
    public PublicacionCatalogo(String id, String titulo, String resumen, String tipo, 
                              String estado, String editorial, LocalDateTime fechaPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.resumen = resumen;
        this.tipo = tipo;
        this.estado = estado;
        this.editorial = editorial;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { 
        this.fechaPublicacion = fechaPublicacion; 
    }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { 
        this.fechaActualizacion = fechaActualizacion; 
    }
    
    public Set<String> getAutores() { return autores; }
    public void setAutores(Set<String> autores) { this.autores = autores; }
    
    public Set<String> getCategorias() { return categorias; }
    public void setCategorias(Set<String> categorias) { this.categorias = categorias; }
}
