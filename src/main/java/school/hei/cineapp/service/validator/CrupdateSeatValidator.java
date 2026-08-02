package school.hei.cineapp.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Seat;
import school.hei.cineapp.repository.JRoomRepository;

@Component
@AllArgsConstructor
public class CrupdateSeatValidator implements Consumer<Seat> {
  private final JRoomRepository jRoomRepository;

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(Seat seat) {
    var sj = new StringJoiner(". ");

    if (isBlank(seat.id())) {
      sj.add("Id is mandatory");
    }
    if (isBlank(seat.number())) {
      sj.add("Number is mandatory");
    }
    if (isBlank(seat.roomId())) {
      sj.add("RoomId is mandatory");
    } else if (!jRoomRepository.existsById(seat.roomId())) {
      sj.add("Room(id=" + seat.roomId() + ") does not exist");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
