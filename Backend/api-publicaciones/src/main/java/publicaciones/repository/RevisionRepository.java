package publicaciones.repository;

import publicaciones.model.Revision;
import publicaciones.enums.EstadoRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, String> {
    
    List<Revision> findByPublicacionId(String publicacionId);
    
    List<Revision> findByRevisorId(String revisorId);
    
    List<Revision> findByEstadoRevision(EstadoRevision estadoRevision);
    
    List<Revision> findByRevisorIdAndEstadoRevision(String revisorId, EstadoRevision estadoRevision);
    
    List<Revision> findByPublicacionIdAndEstadoRevision(String publicacionId, EstadoRevision estadoRevision);
    
    @Query("SELECT r FROM revisiones r WHERE r.fechaLimite < :fecha AND r.estadoRevision = :estado")
    List<Revision> findRevisionesVencidas(@Param("fecha") LocalDateTime fecha, @Param("estado") EstadoRevision estado);
    
    @Query("SELECT r FROM revisiones r WHERE r.publicacionId = :publicacionId ORDER BY r.fechaAsignacion DESC")
    List<Revision> findByPublicacionIdOrderByFechaAsignacionDesc(@Param("publicacionId") String publicacionId);
    
    @Query("SELECT COUNT(r) FROM revisiones r WHERE r.revisorId = :revisorId AND r.estadoRevision = :estado")
    long countByRevisorIdAndEstadoRevision(@Param("revisorId") String revisorId, @Param("estado") EstadoRevision estado);
    
    @Query("SELECT r FROM revisiones r WHERE r.estadoRevision = :estado AND r.fechaAsignacion BETWEEN :fechaInicio AND :fechaFin")
    List<Revision> findByEstadoAndFechaAsignacionBetween(
        @Param("estado") EstadoRevision estado, 
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    Optional<Revision> findTopByPublicacionIdAndEstadoRevisionOrderByFechaAsignacionDesc(
        String publicacionId, EstadoRevision estadoRevision
    );
}
