package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import publicaciones.model.Paper;
import publicaciones.enums.EstadoPublicacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaperRepository extends JpaRepository<Paper, String> {
    
    List<Paper> findByEstadoOrderByFechaCreacionDesc(EstadoPublicacion estado);
    
    List<Paper> findByAutorPrincipalIdOrderByFechaCreacionDesc(String autorId);
    
    Optional<Paper> findByDoi(String doi);
    
    @Query("SELECT p FROM articulos p WHERE p.areaInvestigacion = :area AND p.estado = :estado ORDER BY p.fechaCreacion DESC")
    List<Paper> findByAreaInvestigacionAndEstado(@Param("area") String area, @Param("estado") EstadoPublicacion estado);
    
    @Query("SELECT p FROM articulos p WHERE p.revista = :revista ORDER BY p.anioPublicacion DESC")
    List<Paper> findByRevista(@Param("revista") String revista);
    
    @Query("SELECT p FROM articulos p WHERE p.revisorAsignado = :revisor AND p.estado IN :estados ORDER BY p.fechaEnvioRevision ASC")
    List<Paper> findByRevisorAsignadoAndEstadoIn(@Param("revisor") String revisor, @Param("estados") List<EstadoPublicacion> estados);
    
    @Query("SELECT p FROM articulos p WHERE p.titulo LIKE %:titulo% OR p.abstractIngles LIKE %:keywords% ORDER BY p.fechaCreacion DESC")
    List<Paper> findByTituloOrAbstractContaining(@Param("titulo") String titulo, @Param("keywords") String keywords);
}
