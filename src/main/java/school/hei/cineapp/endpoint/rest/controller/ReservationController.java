package school.hei.cineapp.endpoint.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.cineapp.file.ticket.TicketGenerator;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.security.model.Principal;
import school.hei.cineapp.service.ReservationService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class ReservationController {
  private final ReservationService service;
  private final TicketGenerator ticketGenerator;

  @GetMapping("/reservations")
  public List<Reservation> getAllReservations() {
    return service.findAll();
  }

  @GetMapping("/reservations/{reservationId}")
  public Reservation getReservationById(
      @PathVariable String reservationId, @AuthenticationPrincipal Principal principal) {
    return service.getById(reservationId, principal);
  }

  @GetMapping(value = "/reservations/{reservationId}/ticket", produces = MediaType.ALL_VALUE)
  public ResponseEntity<byte[]> downloadTicket(
      @PathVariable String reservationId, @AuthenticationPrincipal Principal principal) {
    var ticket = service.buildTicketFor(reservationId, principal);
    var file = ticketGenerator.generate(ticket);

    return ResponseEntity.ok()
        .contentType(file.contentType())
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename() + "\"")
        .body(file.content());
  }

  @PutMapping("/reservation")
  public Reservation crupdateReservation(@RequestBody Reservation toSave) {
    return service.save(toSave);
  }
}
