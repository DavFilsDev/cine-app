package school.hei.cineapp.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record Ticket(
    String reservationId,
    String customerName,
    String movieTitle,
    LocalDate projectionDate,
    LocalTime projectionTime,
    String roomName,
    List<String> seats) {}
