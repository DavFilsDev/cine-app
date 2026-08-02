package school.hei.cineapp.model;

import static java.time.Instant.now;

import java.time.Instant;
import lombok.Builder;

@Builder(toBuilder = true)
public record Reservation(
    String id, Instant createdAt, String projectionId, String seatId, String userId) {

  public Reservation {
    if (createdAt == null) {
      createdAt = now();
    }
  }
}
