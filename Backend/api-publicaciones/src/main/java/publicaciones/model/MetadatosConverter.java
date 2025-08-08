package publicaciones.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class MetadatosConverter implements AttributeConverter<MetadatosPublicacion, String> {
    
    private static final Logger logger = LoggerFactory.getLogger(MetadatosConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(MetadatosPublicacion metadatos) {
        if (metadatos == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(metadatos);
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir metadatos a JSON", e);
            return "{}";
        }
    }

    @Override
    public MetadatosPublicacion convertToEntityAttribute(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new MetadatosPublicacion();
        }
        try {
            return objectMapper.readValue(json, MetadatosPublicacion.class);
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir JSON a metadatos", e);
            return new MetadatosPublicacion();
        }
    }
}
