package publicaciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad para rastrear versiones de publicaciones
 */
@Entity(name = "versiones_publicaciones")
@Setter
@Getter
public class VersionPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numeroVersion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "creado_por")
    private String creadoPor;

    // Relación con la publicación original
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_original_id", nullable = false)
    private Publicacion publicacionOriginal;

    // Relación con la nueva versión
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nueva_publicacion_id", nullable = false)
    private Publicacion nuevaPublicacion;
}
