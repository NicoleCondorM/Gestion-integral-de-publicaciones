package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import publicaciones.model.EventoDominio;
import publicaciones.enums.TipoEvento;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoDominioRepository extends JpaRepository<EventoDominio, Long> {
    
    List<EventoDominio> findByAgregadoIdOrderByFechaOcurrenciaDesc(Long agregadoId);
    
    List<EventoDominio> findByTipoEventoOrderByFechaOcurrenciaDesc(TipoEvento tipoEvento);
    
    @Query("SELECT e FROM eventos_dominio e WHERE e.fechaOcurrencia BETWEEN :desde AND :hasta ORDER BY e.fechaOcurrencia DESC")
    List<EventoDominio> findEventosEnRango(@Param("desde") LocalDateTime desde, 
                                          @Param("hasta") LocalDateTime hasta);
    
    @Query("SELECT e FROM eventos_dominio e WHERE e.usuarioId = :usuarioId ORDER BY e.fechaOcurrencia DESC")
    List<EventoDominio> findByUsuarioId(@Param("usuarioId") String usuarioId);
}
