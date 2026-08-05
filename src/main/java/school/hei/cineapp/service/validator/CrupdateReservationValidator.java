package school.hei.cineapp.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.repository.JProjectionRepository;
import school.hei.cineapp.repository.JReservationRepository;
import school.hei.cineapp.repository.JSeatRepository;
import school.hei.cineapp.repository.JUserRepository;

@Component
@AllArgsConstructor
public class CrupdateReservationValidator implements Consumer<Reservation> {
  private final JProjectionRepository jProjectionRepository;
  private final JSeatRepository jSeatRepository;
  private final JUserRepository jUserRepository;
  private final JReservationRepository jReservationRepository;

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(Reservation reservation) {
    var sj = new StringJoiner(". ");

    if (isBlank(reservation.id())) {
      sj.add("Id is mandatory");
    }

    if (isBlank(reservation.projectionId())) {
      sj.add("ProjectionId is mandatory");
    } else if (!jProjectionRepository.existsById(reservation.projectionId())) {
      sj.add("Projection(id=" + reservation.projectionId() + ") does not exist");
    }

    if (isBlank(reservation.seatId())) {
      sj.add("SeatId is mandatory");
    } else if (!jSeatRepository.existsById(reservation.seatId())) {
      sj.add("Seat(id=" + reservation.seatId() + ") does not exist");
    }

    if (isBlank(reservation.userId())) {
      sj.add("UserId is mandatory");
    } else if (!jUserRepository.existsById(reservation.userId())) {
      sj.add("User(id=" + reservation.userId() + ") does not exist");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }

    if (jReservationRepository.existsByProjectionIdAndSeatIdAndIdNot(
        reservation.projectionId(), reservation.seatId(), reservation.id())) {
      throw new IllegalArgumentException(
          "Seat(id="
              + reservation.seatId()
              + ") is already reserved for Projection(id="
              + reservation.projectionId()
              + ")");
    }
  }
}
