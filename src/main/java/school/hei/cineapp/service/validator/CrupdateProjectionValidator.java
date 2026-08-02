package school.hei.cineapp.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Projection;
import school.hei.cineapp.repository.JMovieRepository;
import school.hei.cineapp.repository.JRoomRepository;

@Component
@AllArgsConstructor
public class CrupdateProjectionValidator implements Consumer<Projection> {
  private final JMovieRepository jMovieRepository;
  private final JRoomRepository jRoomRepository;

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(Projection projection) {
    var sj = new StringJoiner(". ");

    if (isBlank(projection.id())) {
      sj.add("Id is mandatory");
    }
    if (projection.datetime() == null) {
      sj.add("Datetime is mandatory");
    }
    if (projection.seatPrice() == null || projection.seatPrice().signum() < 0) {
      sj.add("SeatPrice must be zero or positive");
    }

    if (isBlank(projection.movieId())) {
      sj.add("MovieId is mandatory");
    } else if (!jMovieRepository.existsById(projection.movieId())) {
      sj.add("Movie(id=" + projection.movieId() + ") does not exist");
    }

    if (isBlank(projection.roomId())) {
      sj.add("RoomId is mandatory");
    } else if (!jRoomRepository.existsById(projection.roomId())) {
      sj.add("Room(id=" + projection.roomId() + ") does not exist");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
