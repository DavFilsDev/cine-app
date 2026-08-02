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
import school.hei.cineapp.model.Room;
import school.hei.cineapp.service.RoomService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class RoomController {
  private final RoomService service;

  @GetMapping("/rooms")
  public List<Room> getAllRooms() {
    return service.findAll();
  }

  @GetMapping("/rooms/{roomId}")
  public Room getRoomById(@PathVariable String roomId) {
    return service.getById(roomId);
  }

  @PutMapping("/rooms")
  public Room crupdateRoom(@RequestBody Room toSave) {
    return service.save(toSave);
  }
}
