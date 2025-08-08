package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "autores")
@Setter
@Getter
public class Autor {
    
    @Id
    @Column(columnDefinition = "VARCHAR(26)")
    private String id = UlidCreator.getUlid().toString();

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 100, unique = true)
    private String email;

    @Column(length = 100)
    private String nacionalidad;

    @Column(name = "afiliacion", length = 200)
    private String institucion;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(unique = true, nullable = false, length = 50)
    private String orcid;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    // Roles del autor como lista JSON
    @ElementCollection
    @CollectionTable(name = "autor_roles", 
                    joinColumns = @JoinColumn(name = "autor_id"))
    @Column(name = "rol")
    private List<String> roles = new ArrayList<>();

    // Campos adicionales profesionales
    @Column(name = "titulo_academico", length = 100)
    private String tituloAcademico; // Dr., PhD, MSc, etc.

    @Column(name = "departamento", length = 200)
    private String departamento;

    @Column(name = "especialidades")
    private String especialidades; // Áreas de especialización

    @Column(name = "pagina_web", length = 500)
    private String paginaWeb;

    @Column(name = "perfil_linkedin", length = 500)
    private String perfilLinkedin;

    @Column(name = "perfil_researchgate", length = 500)
    private String perfilResearchGate;

    @Column(name = "h_index")
    private Integer hIndex; // Índice h del autor

    @Column(name = "numero_publicaciones")
    private Integer numeroPublicaciones = 0;

    @Column(name = "numero_citaciones")
    private Integer numeroCitaciones = 0;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "activo")
    private Boolean activo = true;

    // Relaciones con publicaciones (mantenidas por compatibilidad)
    @OneToMany(mappedBy = "autor")
    @JsonIgnore
    private List<Libro> libros = new ArrayList<>();

    @OneToMany(mappedBy = "autor")
    @JsonIgnore
    private List<Paper> articulos = new ArrayList<>();

    // Constructores
    public Autor() {
        this.id = UlidCreator.getUlid().toString();
    }

    // Métodos de utilidad para roles
    public void agregarRol(String rol) {
        if (rol != null && !rol.trim().isEmpty() && !roles.contains(rol.trim())) {
            roles.add(rol.trim());
        }
    }

    public void removerRol(String rol) {
        roles.remove(rol);
    }

    public boolean tieneRol(String rol) {
        return roles.contains(rol);
    }

    // Métodos de utilidad
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getNombreComoCita() {
        // Formato: Apellido, N.
        StringBuilder cita = new StringBuilder(apellido);
        if (nombre != null && !nombre.isEmpty()) {
            cita.append(", ").append(nombre.charAt(0)).append(".");
        }
        return cita.toString();
    }

    public void incrementarPublicaciones() {
        this.numeroPublicaciones = (this.numeroPublicaciones != null ? this.numeroPublicaciones : 0) + 1;
    }

    public void actualizarCitaciones(Integer nuevasCitaciones) {
        this.numeroCitaciones = nuevasCitaciones;
    }

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
    }
}
