package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import publicaciones.enums.EstadoPublicacion;
import publicaciones.enums.TipoPublicacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "publicaciones")
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Publicacion {

    @Id
    @Column(columnDefinition = "VARCHAR(26)")
    private String id = UlidCreator.getUlid().toString();

    @Column(nullable = false, length = 500)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String resumen;

    @ElementCollection
    @CollectionTable(name = "publicacion_palabras_clave", 
                    joinColumns = @JoinColumn(name = "publicacion_id"))
    @Column(name = "palabra_clave")
    private List<String> palabrasClave = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPublicacion estado = EstadoPublicacion.BORRADOR;

    @Column(nullable = false, length = 10)
    private String versionActual = "v1.0";

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ID del autor principal como String UUID
    @Column(name = "autor_principal_id", nullable = false)
    private String autorPrincipalId;

    // Lista de IDs de coautores
    @ElementCollection
    @CollectionTable(name = "publicacion_coautores", 
                    joinColumns = @JoinColumn(name = "publicacion_id"))
    @Column(name = "coautor_id")
    private List<String> coAutoresIds = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPublicacion tipo;

    // Metadatos como JSONB (será manejado por Jackson automáticamente)
    @Column(columnDefinition = "TEXT")
    @Convert(converter = MetadatosConverter.class)
    private MetadatosPublicacion metadatos = new MetadatosPublicacion();

    // Campos adicionales para información básica
    private Integer anioPublicacion;
    private String editorial;

    // Campos para ciclo de vida de revisión
    @Column(name = "fecha_envio_revision")
    private LocalDateTime fechaEnvioRevision;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(name = "comentarios_revision", columnDefinition = "TEXT")
    private String comentariosRevision;

    @Column(name = "revisor_asignado")
    private String revisorAsignado;

    // Relación con versiones anteriores
    @OneToMany(mappedBy = "publicacionOriginal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VersionPublicacion> versiones = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_original_id")
    @JsonIgnore
    private Publicacion publicacionOriginal;

    // Relación con el autor principal (mantenido para compatibilidad)
    @ManyToOne
    @JoinColumn(name = "autor_id")
    @JsonIgnore
    private Autor autor;

    // Constructores
    public Publicacion() {
        this.id = UlidCreator.getUlid().toString();
    }

    // Métodos de negocio para el ciclo de vida
    public boolean puedeTransicionarA(EstadoPublicacion nuevoEstado) {
        return this.estado.puedeTransicionarA(nuevoEstado);
    }

    public void cambiarEstado(EstadoPublicacion nuevoEstado) {
        if (!puedeTransicionarA(nuevoEstado)) {
            throw new IllegalStateException(
                String.format("No se puede cambiar de estado %s a %s", 
                             this.estado, nuevoEstado)
            );
        }
        this.estado = nuevoEstado;
        this.fechaActualizacion = LocalDateTime.now();
        
        // Actualizar fechas específicas según el estado
        switch (nuevoEstado) {
            case EN_REVISION -> this.fechaEnvioRevision = LocalDateTime.now();
            case APROBADO -> this.fechaAprobacion = LocalDateTime.now();
            case PUBLICADO -> this.fechaPublicacion = LocalDateTime.now();
            case BORRADOR, CAMBIOS_SOLICITADOS, RETIRADO -> {
                // No se actualizan fechas específicas para estos estados
            }
        }
    }

    public String generarNuevaVersion() {
        String[] partes = this.versionActual.substring(1).split("\\.");
        int versionMayor = Integer.parseInt(partes[0]);
        int versionMenor = partes.length > 1 ? Integer.parseInt(partes[1]) : 0;
        return String.format("v%d.%d", versionMayor, versionMenor + 1);
    }

    // Métodos de utilidad para palabras clave
    public void agregarPalabraClave(String palabra) {
        if (palabra != null && !palabra.trim().isEmpty() && !palabrasClave.contains(palabra.trim())) {
            palabrasClave.add(palabra.trim());
        }
    }

    public void removerPalabraClave(String palabra) {
        palabrasClave.remove(palabra);
    }

    // Métodos para manejo de coautores
    public void agregarCoautor(String coautorId) {
        if (coautorId != null && !coAutoresIds.contains(coautorId)) {
            coAutoresIds.add(coautorId);
        }
    }

    public void removerCoautor(String coautorId) {
        coAutoresIds.remove(coautorId);
    }

    @PreUpdate
    private void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }
}
