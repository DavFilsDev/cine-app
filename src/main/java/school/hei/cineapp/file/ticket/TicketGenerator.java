package school.hei.cineapp.file.ticket;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Ticket;

@Component
public class TicketGenerator {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  public GeneratedFile generate(Ticket ticket) {
    var content = render(ticket);
    var filename = "ticket-reservation-" + ticket.reservationId() + ".txt";
    return new GeneratedFile(
        content.getBytes(StandardCharsets.UTF_8), filename, MediaType.TEXT_PLAIN);
  }

  private String render(Ticket ticket) {
    var seatsLabel = String.join(", ", ticket.seats());
    return """
           ================================
                   CINE-APP - TICKET
           ================================

           Reservation : %s
           Client      : %s

           Film        : %s
           Date        : %s
           Heure       : %s
           Salle       : %s
           Siege(s)    : %s

           --------------------------------
           Merci de votre confiance, bon film !
           ================================
           """
        .formatted(
            ticket.reservationId(),
            ticket.customerName(),
            ticket.movieTitle(),
            ticket.projectionDate().format(DATE_FORMATTER),
            ticket.projectionTime().format(TIME_FORMATTER),
            ticket.roomName(),
            seatsLabel);
  }
}
