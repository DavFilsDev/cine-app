package school.hei.cineapp.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder(toBuilder = true)
public record Projection(
    String id, Instant datetime, BigDecimal seatPrice, String movieId, String roomId) {}
