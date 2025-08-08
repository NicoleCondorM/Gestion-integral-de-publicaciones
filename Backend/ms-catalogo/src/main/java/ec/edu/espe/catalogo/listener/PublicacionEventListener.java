package ec.edu.espe.catalogo.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.catalogo.domain.PublicacionCatalogo;
import ec.edu.espe.catalogo.repository.PublicacionCatalogoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
public class PublicacionEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(PublicacionEventListener.class);
    
    @Autowired
    private PublicacionCatalogoRepository repository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @RabbitListener(queues = "publicacion.publicada")
    @Transactional
    public void manejarPublicacionPublicada(String mensaje) {
        try {
            logger.info("Procesando evento de publicación publicada: {}", mensaje);
            
            JsonNode evento = objectMapper.readTree(mensaje);
            String eventoTipo = evento.get("eventType").asText();
            
            if ("PUBLICACION_PUBLICADA".equals(eventoTipo)) {
                JsonNode publicacionData = evento.get("publicacion");
                
                PublicacionCatalogo catalogoItem = new PublicacionCatalogo();
                catalogoItem.setId(publicacionData.get("id").asText());
                catalogoItem.setTitulo(publicacionData.get("titulo").asText());
                catalogoItem.setResumen(publicacionData.get("resumen").asText());
                catalogoItem.setTipo(publicacionData.get("tipo").asText());
                catalogoItem.setEstado("PUBLICADO");
                catalogoItem.setEditorial(publicacionData.get("editorial").asText());
                
                // Fecha de publicación
                String fechaStr = publicacionData.get("fechaPublicacion").asText();
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                catalogoItem.setFechaPublicacion(fecha);
                catalogoItem.setFechaActualizacion(LocalDateTime.now());
                
                // Autores
                Set<String> autores = new HashSet<>();
                JsonNode autoresNode = publicacionData.get("autores");
                if (autoresNode != null && autoresNode.isArray()) {
                    autoresNode.forEach(autor -> autores.add(autor.asText()));
                }
                catalogoItem.setAutores(autores);
                
                // Categorías
                Set<String> categorias = new HashSet<>();
                JsonNode categoriasNode = publicacionData.get("categorias");
                if (categoriasNode != null && categoriasNode.isArray()) {
                    categoriasNode.forEach(categoria -> categorias.add(categoria.asText()));
                }
                catalogoItem.setCategorias(categorias);
                
                repository.save(catalogoItem);
                logger.info("Publicación agregada al catálogo: {}", catalogoItem.getId());
            }
            
        } catch (Exception e) {
            logger.error("Error procesando evento de publicación: {}", e.getMessage(), e);
        }
    }
    
    @RabbitListener(queues = "publicacion.retirada")
    @Transactional
    public void manejarPublicacionRetirada(String mensaje) {
        try {
            logger.info("Procesando evento de publicación retirada: {}", mensaje);
            
            JsonNode evento = objectMapper.readTree(mensaje);
            String eventoTipo = evento.get("eventType").asText();
            
            if ("PUBLICACION_RETIRADA".equals(eventoTipo)) {
                String publicacionId = evento.get("publicacionId").asText();
                
                repository.findById(publicacionId).ifPresent(publicacion -> {
                    publicacion.setEstado("RETIRADO");
                    publicacion.setFechaActualizacion(LocalDateTime.now());
                    repository.save(publicacion);
                    logger.info("Publicación retirada del catálogo: {}", publicacionId);
                });
            }
            
        } catch (Exception e) {
            logger.error("Error procesando evento de retiro: {}", e.getMessage(), e);
        }
    }
}
