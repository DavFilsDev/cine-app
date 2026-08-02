package school.hei.cineapp.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"projection\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JProjection {
  @Id private String id;

  private Instant datetime;
  private BigDecimal seatPrice;
  private String movieId;
  private String roomId;
}
