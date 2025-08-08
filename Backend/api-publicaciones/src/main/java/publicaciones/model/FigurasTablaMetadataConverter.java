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
public class FigurasTablaMetadataConverter implements AttributeConverter<List<FiguraTablaMetadata>, String> {
    
    private static final Logger logger = LoggerFactory.getLogger(FigurasTablaMetadataConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<FiguraTablaMetadata> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir metadata de figuras/tablas a JSON", e);
            return "[]";
        }
    }

    @Override
    public List<FiguraTablaMetadata> convertToEntityAttribute(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<FiguraTablaMetadata>>() {});
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir JSON a metadata de figuras/tablas", e);
            return new ArrayList<>();
        }
    }
}
