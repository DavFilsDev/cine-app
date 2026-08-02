package school.hei.cineapp.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Room;

@Component
public class CrupdateRoomValidator implements Consumer<Room> {
  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(Room room) {
    var sj = new StringJoiner(". ");

    if (isBlank(room.id())) {
      sj.add("Id is mandatory");
    }
    if (isBlank(room.number())) {
      sj.add("Number is mandatory");
    }
    if (room.capacity() <= 0) {
      sj.add("Capacity must be positive");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
