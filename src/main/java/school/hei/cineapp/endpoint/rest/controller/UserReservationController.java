package school.hei.cineapp.endpoint.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.service.ReservationService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class UserReservationController {
  private final ReservationService service;

  @GetMapping("/users/{uid}/reservations")
  public List<Reservation> getOwnReservations(@PathVariable String uid) {

    return service.findAllByUserId(uid);
  }

  @PutMapping("/users/{uid}/reservations")
  public Reservation crupdateOwnReservation(
      @PathVariable String uid, @RequestBody Reservation toSave) {
    return service.save(toSave.toBuilder().userId(uid).build());
  }
}
