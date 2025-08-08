package publicaciones.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Converter
public class HistorialCambiosConverter implements AttributeConverter<List<CambioRevision>, String> {
    
    private static final Logger logger = LoggerFactory.getLogger(HistorialCambiosConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<CambioRevision> cambios) {
        if (cambios == null || cambios.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(cambios);
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir historial de cambios a JSON", e);
            return "[]";
        }
    }

    @Override
    public List<CambioRevision> convertToEntityAttribute(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<CambioRevision>>() {});
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir JSON a historial de cambios", e);
            return new ArrayList<>();
        }
    }
}
