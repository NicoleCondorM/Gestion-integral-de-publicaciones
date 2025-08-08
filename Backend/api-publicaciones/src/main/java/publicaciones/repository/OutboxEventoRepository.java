package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import publicaciones.model.OutboxEvento;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxEventoRepository extends JpaRepository<OutboxEvento, Long> {
    
    List<OutboxEvento> findByProcesadoFalseOrderByFechaCreacionAsc();
    
    @Query("SELECT o FROM outbox_eventos o WHERE o.procesado = false AND o.intentos < 5 ORDER BY o.fechaCreacion ASC")
    List<OutboxEvento> findEventosPendientesParaProcesar();
    
    @Query("SELECT o FROM outbox_eventos o WHERE o.procesado = false AND o.intentos >= 5")
    List<OutboxEvento> findEventosFallidos();
    
    @Query("SELECT o FROM outbox_eventos o WHERE o.fechaCreacion < :fecha AND o.procesado = true")
    List<OutboxEvento> findEventosProcesadosAnterioresA(LocalDateTime fecha);
    
    List<OutboxEvento> findByAgregadoIdOrderByFechaCreacionDesc(Long agregadoId);
}
