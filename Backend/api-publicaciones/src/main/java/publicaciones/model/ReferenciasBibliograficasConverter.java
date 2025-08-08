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
public class ReferenciasBibliograficasConverter implements AttributeConverter<List<ReferenciaBibliografica>, String> {
    
    private static final Logger logger = LoggerFactory.getLogger(ReferenciasBibliograficasConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ReferenciaBibliografica> referencias) {
        if (referencias == null || referencias.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(referencias);
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir referencias a JSON", e);
            return "[]";
        }
    }

    @Override
    public List<ReferenciaBibliografica> convertToEntityAttribute(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<ReferenciaBibliografica>>() {});
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir JSON a referencias", e);
            return new ArrayList<>();
        }
    }
}
