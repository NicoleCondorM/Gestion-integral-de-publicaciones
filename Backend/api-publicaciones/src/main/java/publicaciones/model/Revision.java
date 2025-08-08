package publicaciones.model;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import publicaciones.enums.EstadoRevision;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "revisiones")
@Setter
@Getter
public class Revision {
    
    @Id
    @Column(columnDefinition = "VARCHAR(26)")
    private String id = UlidCreator.getUlid().toString();

    @Column(name = "publicacion_id", nullable = false)
    private String publicacionId;

    @Column(name = "revisor_id", nullable = false)
    private String revisorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_revision", nullable = false)
    private EstadoRevision estadoRevision = EstadoRevision.PENDIENTE;

    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "comentarios_publicos", columnDefinition = "TEXT")
    private String comentariosPublicos; // Comentarios que verá el autor

    @Column(name = "comentarios_privados", columnDefinition = "TEXT")
    private String comentariosPrivados; // Comentarios solo para el editor

    @Column(name = "puntuacion_general")
    private Integer puntuacionGeneral; // 1-10

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion = LocalDateTime.now();

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;

    // Historial de cambios como JSON
    @Column(name = "historial_cambios", columnDefinition = "TEXT")
    @Convert(converter = HistorialCambiosConverter.class)
    private List<CambioRevision> historialCambios = new ArrayList<>();

    // Criterios de evaluación específicos
    @Column(name = "calidad_contenido")
    private Integer calidadContenido; // 1-5

    @Column(name = "originalidad")
    private Integer originalidad; // 1-5

    @Column(name = "metodologia")
    private Integer metodologia; // 1-5

    @Column(name = "redaccion_estilo")
    private Integer redaccionEstilo; // 1-5

    @Column(name = "referencias_bibliografia")
    private Integer referenciasBibliografia; // 1-5

    // Recomendaciones específicas
    @Column(name = "requiere_revision_menor")
    private Boolean requiereRevisionMenor = false;

    @Column(name = "requiere_revision_mayor")
    private Boolean requiereRevisionMayor = false;

    @Column(name = "recomendacion_publicacion")
    private String recomendacionPublicacion; // ACEPTAR, RECHAZAR, REVISION_MENOR, REVISION_MAYOR

    // Campos adicionales
    @Column(name = "confidencial")
    private Boolean confidencial = false;

    @Column(name = "revisor_anonimo")
    private Boolean revisorAnonimo = true;

    @Column(name = "palabras_clave_sugeridas")
    private String palabrasClaveySugeridas;

    @Column(name = "sugerencias_mejora", columnDefinition = "TEXT")
    private String sugerenciasMejora;

    // Relación con la publicación (opcional, usando ID string)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", insertable = false, updatable = false)
    private Publicacion publicacion;

    // Constructores
    public Revision() {
        this.id = UlidCreator.getUlid().toString();
    }

    public Revision(String publicacionId, String revisorId) {
        this();
        this.publicacionId = publicacionId;
        this.revisorId = revisorId;
    }

    // Métodos de negocio
    public void iniciarRevision() {
        if (this.estadoRevision == EstadoRevision.PENDIENTE) {
            this.estadoRevision = EstadoRevision.EN_PROCESO;
            this.fechaInicio = LocalDateTime.now();
            agregarCambio("Revisión iniciada", "SISTEMA");
        }
    }

    public void completarRevision(String comentariosFinal, String recomendacion) {
        this.estadoRevision = EstadoRevision.COMPLETADA;
        this.fechaFinalizacion = LocalDateTime.now();
        this.comentarios = comentariosFinal;
        this.recomendacionPublicacion = recomendacion;
        agregarCambio("Revisión completada con recomendación: " + recomendacion, "REVISOR");
    }

    public void marcarComoAceptada() {
        this.estadoRevision = EstadoRevision.ACEPTADA;
        agregarCambio("Revisión aceptada por el editor", "EDITOR");
    }

    public void marcarComoRechazada(String motivo) {
        this.estadoRevision = EstadoRevision.RECHAZADA;
        agregarCambio("Revisión rechazada: " + motivo, "EDITOR");
    }

    public void devolverParaRevision(String motivo) {
        this.estadoRevision = EstadoRevision.DEVUELTA;
        agregarCambio("Devuelta para revisión: " + motivo, "EDITOR");
    }

    // Método para agregar cambios al historial
    public void agregarCambio(String descripcion, String usuario) {
        CambioRevision cambio = new CambioRevision();
        cambio.setFecha(LocalDateTime.now());
        cambio.setDescripcion(descripcion);
        cambio.setUsuario(usuario);
        cambio.setEstadoAnterior(this.estadoRevision != null ? this.estadoRevision.name() : "NUEVO");
        
        historialCambios.add(cambio);
    }

    // Métodos de utilidad
    public boolean estaVencida() {
        return fechaLimite != null && LocalDateTime.now().isAfter(fechaLimite);
    }

    public long getDiasTranscurridos() {
        LocalDateTime inicio = fechaInicio != null ? fechaInicio : fechaAsignacion;
        return java.time.Duration.between(inicio, LocalDateTime.now()).toDays();
    }

    public Double getCalificacionPromedio() {
        int suma = 0;
        int count = 0;
        
        if (calidadContenido != null) { suma += calidadContenido; count++; }
        if (originalidad != null) { suma += originalidad; count++; }
        if (metodologia != null) { suma += metodologia; count++; }
        if (redaccionEstilo != null) { suma += redaccionEstilo; count++; }
        if (referenciasBibliografia != null) { suma += referenciasBibliografia; count++; }
        
        return count > 0 ? (double) suma / count : null;
    }

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
    }
}
