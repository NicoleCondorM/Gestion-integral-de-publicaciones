package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import publicaciones.enums.TipoPublicacion;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "libros")
@Setter
@Getter
public class Libro extends Publicacion {
    
    private String genero;
    
    @Column(name = "numero_paginas")
    private Integer numeroPaginas;

    @Column(name = "numero_edicion")
    private Integer numeroEdicion = 1;

    @Column(name = "idioma")
    private String idioma;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "formato")
    private String formato; // Físico, Digital, Ambos

    @Column(name = "tabla_contenidos", columnDefinition = "TEXT")
    private String tablaContenidos;

    // Capítulos como estructura JSON
    @Column(name = "capitulos", columnDefinition = "TEXT")
    @Convert(converter = CapitulosConverter.class)
    private List<Capitulo> capitulos = new ArrayList<>();

    // Relación con autor principal (mantenida por compatibilidad)
    @ManyToOne
    @JoinColumn(name = "autor_id")
    @JsonIgnore
    private Autor autor;

    // Relación con coautores si es necesario
    @ManyToMany
    @JoinTable(
        name = "libro_coautores",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    @JsonIgnore
    private List<Autor> coautores = new ArrayList<>();

    // Constructor
    public Libro() {
        super();
        this.setTipo(TipoPublicacion.LIBRO);
    }

    // Métodos específicos para libros
    public void agregarCoautor(Autor coautor) {
        if (!coautores.contains(coautor)) {
            coautores.add(coautor);
        }
    }

    public void removerCoautor(Autor coautor) {
        coautores.remove(coautor);
    }

    public String getAutoresCompletos() {
        StringBuilder autores = new StringBuilder();
        if (autor != null) {
            autores.append(autor.getNombre()).append(" ").append(autor.getApellido());
        }
        
        if (!coautores.isEmpty()) {
            for (Autor coautor : coautores) {
                autores.append(", ").append(coautor.getNombre()).append(" ").append(coautor.getApellido());
            }
        }
        
        return autores.toString();
    }

    // Métodos para manejo de capítulos
    public void agregarCapitulo(Capitulo capitulo) {
        if (capitulo != null) {
            capitulos.add(capitulo);
        }
    }

    public void removerCapitulo(Integer numeroCapitulo) {
        capitulos.removeIf(cap -> numeroCapitulo.equals(cap.getNumero()));
    }

    public Capitulo obtenerCapitulo(Integer numero) {
        return capitulos.stream()
                .filter(cap -> numero.equals(cap.getNumero()))
                .findFirst()
                .orElse(null);
    }

    public Integer getTotalPaginasCalculadas() {
        return capitulos.stream()
                .mapToInt(cap -> cap.getNumeroPaginas() != null ? cap.getNumeroPaginas() : 0)
                .sum();
    }

    public Integer getTotalCapitulos() {
        return capitulos.size();
    }
}
