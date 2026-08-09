package school.hei.cineapp.service;

import static school.hei.cineapp.security.model.UserRole.EMPLOYEE;
import static school.hei.cineapp.security.model.UserRole.MANAGER;

import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.model.Ticket;
import school.hei.cineapp.repository.JMovieRepository;
import school.hei.cineapp.repository.JProjectionRepository;
import school.hei.cineapp.repository.JReservationRepository;
import school.hei.cineapp.repository.JRoomRepository;
import school.hei.cineapp.repository.JSeatRepository;
import school.hei.cineapp.repository.JUserRepository;
import school.hei.cineapp.repository.mapper.JReservationMapper;
import school.hei.cineapp.security.model.Principal;
import school.hei.cineapp.service.validator.CrupdateReservationValidator;

@Service
@AllArgsConstructor
public class ReservationService {
  private final JReservationRepository jRepository;
  private final JReservationMapper jMapper;
  private final CrupdateReservationValidator crupdateValidator;
  private final JProjectionRepository jProjectionRepository;
  private final JSeatRepository jSeatRepository;
  private final JMovieRepository jMovieRepository;
  private final JRoomRepository jRoomRepository;
  private final JUserRepository jUserRepository;

  public List<Reservation> findAll() {
    return jRepository.findAll().stream().map(jMapper::toDomain).toList();
  }

  public List<Reservation> findAllByUserId(String userId) {
    return jRepository.findByUserId(userId).stream().map(jMapper::toDomain).toList();
  }

  public Reservation getById(String id, Principal requester) {
    var reservation =
        jRepository
            .findById(id)
            .map(jMapper::toDomain)
            .orElseThrow(() -> new NoSuchElementException("Reservation(id=" + id + ") not found"));

    var role = requester.user().getRole();
    var isStaff = role == EMPLOYEE || role == MANAGER;
    var isOwner = reservation.userId().equals(requester.user().getId());

    if (!isStaff && !isOwner) {
      throw new AccessDeniedException("You are not allowed to access this reservation");
    }

    return reservation;
  }

  public Reservation save(Reservation toSave) {
    crupdateValidator.accept(toSave);

    var entity = jMapper.toEntity(toSave);
    return jMapper.toDomain(jRepository.save(entity));
  }

  public Ticket buildTicketFor(String reservationId, Principal requester) {
    var reservation = getById(reservationId, requester);

    var projection =
        jProjectionRepository
            .findById(reservation.projectionId())
            .orElseThrow(
                () ->
                    new NoSuchElementException(
                        "Projection(id=" + reservation.projectionId() + ") not found"));
    var seat =
        jSeatRepository
            .findById(reservation.seatId())
            .orElseThrow(
                () ->
                    new NoSuchElementException("Seat(id=" + reservation.seatId() + ") not found"));
    var movie =
        jMovieRepository
            .findById(projection.getMovieId())
            .orElseThrow(
                () ->
                    new NoSuchElementException(
                        "Movie(id=" + projection.getMovieId() + ") not found"));
    var room =
        jRoomRepository
            .findById(projection.getRoomId())
            .orElseThrow(
                () ->
                    new NoSuchElementException(
                        "Room(id=" + projection.getRoomId() + ") not found"));

    var owner =
        jUserRepository
            .findById(reservation.userId())
            .orElseThrow(
                () ->
                    new NoSuchElementException("User(id=" + reservation.userId() + ") not found"));

    var projectionDateTime = projection.getDatetime().atZone(ZoneId.systemDefault());

    return Ticket.builder()
        .reservationId(reservation.id())
        .customerName(owner.getFirstName() + " " + owner.getLastName())
        .movieTitle(movie.getTitle())
        .projectionDate(projectionDateTime.toLocalDate())
        .projectionTime(projectionDateTime.toLocalTime())
        .roomName(room.getNumber())
        .seats(List.of(seat.getNumber()))
        .build();
  }
}
