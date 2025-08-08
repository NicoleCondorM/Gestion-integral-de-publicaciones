package ec.edu.espe.catalogo.repository;

import ec.edu.espe.catalogo.domain.PublicacionCatalogo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionCatalogoRepository extends JpaRepository<PublicacionCatalogo, String> {
    
    Page<PublicacionCatalogo> findByEstado(String estado, Pageable pageable);
    
    Page<PublicacionCatalogo> findByTipo(String tipo, Pageable pageable);
    
    Page<PublicacionCatalogo> findByEstadoAndTipo(String estado, String tipo, Pageable pageable);
    
    @Query("SELECT p FROM PublicacionCatalogo p WHERE " +
           "LOWER(p.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.resumen) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    Page<PublicacionCatalogo> buscarPorTexto(@Param("busqueda") String busqueda, Pageable pageable);
    
    @Query("SELECT p FROM PublicacionCatalogo p JOIN p.autores a WHERE " +
           "LOWER(a) LIKE LOWER(CONCAT('%', :autor, '%'))")
    Page<PublicacionCatalogo> findByAutoresContaining(@Param("autor") String autor, Pageable pageable);
    
    @Query("SELECT p FROM PublicacionCatalogo p JOIN p.categorias c WHERE " +
           "LOWER(c) LIKE LOWER(CONCAT('%', :categoria, '%'))")
    Page<PublicacionCatalogo> findByCategoriasContaining(@Param("categoria") String categoria, Pageable pageable);
    
    @Query("SELECT DISTINCT categoria FROM PublicacionCatalogo p JOIN p.categorias categoria")
    List<String> findAllCategorias();
    
    @Query("SELECT DISTINCT autor FROM PublicacionCatalogo p JOIN p.autores autor")
    List<String> findAllAutores();
}
