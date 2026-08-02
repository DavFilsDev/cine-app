package school.hei.cineapp.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import school.hei.cineapp.model.Genre;

@Entity
@Table(name = "\"movie\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JMovie {
  @Id private String id;

  private String title;

  @Convert(converter = GenreListConverter.class)
  @Column(name = "genre")
  private List<Genre> genre;

  private String description;

  // Hibernate 6 maps java.time.Duration to a numeric (nanoseconds) column natively.
  private Duration duration;
}
