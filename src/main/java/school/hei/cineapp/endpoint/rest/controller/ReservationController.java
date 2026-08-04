package school.hei.cineapp.endpoint.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.security.model.Principal;
import school.hei.cineapp.service.ReservationService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class ReservationController {
  private final ReservationService service;

  @GetMapping("/reservations")
  public List<Reservation> getAllReservations() {
    return service.findAll();
  }

  @GetMapping("/reservations/{reservationId}")
  public Reservation getReservationById(
      @PathVariable String reservationId, @AuthenticationPrincipal Principal principal) {
    return service.getById(reservationId, principal);
  }

  @PutMapping("/reservation")
  public Reservation crupdateReservation(@RequestBody Reservation toSave) {
    return service.save(toSave);
  }
}
