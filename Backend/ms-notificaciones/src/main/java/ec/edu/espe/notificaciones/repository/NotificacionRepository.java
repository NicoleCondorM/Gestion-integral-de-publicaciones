package ec.edu.espe.notificaciones.repository;

import ec.edu.espe.notificaciones.domain.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    List<Notificacion> findByEstado(Notificacion.EstadoNotificacion estado);
    
    List<Notificacion> findByDestinatario(String destinatario);
    
    List<Notificacion> findByTipo(Notificacion.TipoNotificacion tipo);
    
    List<Notificacion> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    long countByEstado(Notificacion.EstadoNotificacion estado);
}
