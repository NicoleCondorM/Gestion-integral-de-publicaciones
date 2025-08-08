package publicaciones.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicaciones.dto.NotificacionDto;

@Service
public class NotificacionProducer {

    @Autowired
    private RabbitTemplate rabbit;

    @Autowired
    private ObjectMapper mapper;

    public void enviarNotificacion(String mensaje, String tipo){
        try {
            NotificacionDto dto = new NotificacionDto(mensaje, tipo);
            String json = mapper.writeValueAsString(dto);
            rabbit.convertAndSend("queue.notificaciones", json);
            System.out.println("---------------------------------------------------------------");
            System.out.println("Enviando notificacion..."+json);
            System.out.println("---------------------------------------------------------------");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
