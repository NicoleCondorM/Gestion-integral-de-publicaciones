package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publicaciones.model.VersionPublicacion;

import java.util.List;

@Repository
public interface VersionPublicacionRepository extends JpaRepository<VersionPublicacion, Long> {
    
    List<VersionPublicacion> findByPublicacionOriginalIdOrderByFechaCreacionDesc(String publicacionId);
    
    List<VersionPublicacion> findByCreadoPorOrderByFechaCreacionDesc(String creadoPor);
}
