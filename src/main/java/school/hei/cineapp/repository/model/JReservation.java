package school.hei.cineapp.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"reservation\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JReservation {
  @Id private String id;

  private Instant createdAt;
  private String projectionId;
  private String seatId;
  private String userId;
}
