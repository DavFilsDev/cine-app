package school.hei.cineapp.repository.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import school.hei.cineapp.model.Genre;

@Converter
public class GenreListConverter implements AttributeConverter<List<Genre>, String> {
  private static final String SEPARATOR = ",";

  @Override
  public String convertToDatabaseColumn(List<Genre> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "";
    }
    return attribute.stream().map(Enum::name).reduce((a, b) -> a + SEPARATOR + b).orElse("");
  }

  @Override
  public List<Genre> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) {
      return List.of();
    }
    return Arrays.stream(dbData.split(SEPARATOR)).map(Genre::valueOf).toList();
  }
}
