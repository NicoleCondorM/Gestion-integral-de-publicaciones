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
public class CapitulosConverter implements AttributeConverter<List<Capitulo>, String> {
    
    private static final Logger logger = LoggerFactory.getLogger(CapitulosConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Capitulo> capitulos) {
        if (capitulos == null || capitulos.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(capitulos);
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir capítulos a JSON", e);
            return "[]";
        }
    }

    @Override
    public List<Capitulo> convertToEntityAttribute(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Capitulo>>() {});
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir JSON a capítulos", e);
            return new ArrayList<>();
        }
    }
}
