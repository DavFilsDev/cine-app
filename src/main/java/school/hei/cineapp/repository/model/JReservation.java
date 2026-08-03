package school.hei.cineapp.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"reservation\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JReservation {
  @Id private UUID id;

  private String projectionId;
  private String seatId;
  private String userId;
  private Instant createdAt;
}
