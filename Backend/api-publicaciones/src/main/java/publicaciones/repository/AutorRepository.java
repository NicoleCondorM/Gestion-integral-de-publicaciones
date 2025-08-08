package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import publicaciones.model.Autor;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, String> {
    
    Optional<Autor> findByEmail(String email);
    
    Optional<Autor> findByOrcid(String orcid);
    
    List<Autor> findByInstitucion(String institucion);
    
    @Query("SELECT a FROM autores a WHERE a.nombre LIKE %:nombre% OR a.apellido LIKE %:apellido%")
    List<Autor> findByNombreOrApellidoContaining(@Param("nombre") String nombre, @Param("apellido") String apellido);
}
