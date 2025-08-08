package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import publicaciones.model.Libro;
import publicaciones.enums.EstadoPublicacion;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {
    
    List<Libro> findByEstadoOrderByFechaCreacionDesc(EstadoPublicacion estado);
    
    List<Libro> findByAutorPrincipalIdOrderByFechaCreacionDesc(String autorId);
    
    List<Libro> findByAutorPrincipalId(String autorId);
    
    List<Libro> findByEstado(EstadoPublicacion estado);
    
    @Query("SELECT l FROM libros l WHERE l.titulo LIKE %:titulo% ORDER BY l.fechaCreacion DESC")
    List<Libro> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);
    
    @Query("SELECT l FROM libros l WHERE l.estado = :estado ORDER BY l.fechaCreacion DESC")
    List<Libro> findByEstadoOrderByCreacion(@Param("estado") EstadoPublicacion estado);
}
