package school.hei.cineapp.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.cineapp.model.Seat;
import school.hei.cineapp.service.SeatService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class SeatController {
  private final SeatService service;

  @GetMapping("/rooms/{roomId}/seats")
  public List<Seat> getSeatsByRoom(@PathVariable String roomId) {
    return service.findAllByRoomId(roomId);
  }

  @PutMapping("/rooms/{roomId}/seats")
  public Seat crupdateSeat(@PathVariable String roomId, @RequestBody Seat toSave) {
    // roomId always comes from the path, never trust a client-supplied body value here.
    return service.save(toSave.toBuilder().roomId(roomId).build());
  }
}
