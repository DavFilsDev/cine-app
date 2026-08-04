package school.hei.cineapp.service;

import static school.hei.cineapp.security.model.UserRole.EMPLOYEE;
import static school.hei.cineapp.security.model.UserRole.MANAGER;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import school.hei.cineapp.model.Reservation;
import school.hei.cineapp.repository.JReservationRepository;
import school.hei.cineapp.repository.mapper.JReservationMapper;
import school.hei.cineapp.security.model.Principal;
import school.hei.cineapp.service.validator.CrupdateReservationValidator;

@Service
@AllArgsConstructor
public class ReservationService {
  private final JReservationRepository jRepository;
  private final JReservationMapper jMapper;
  private final CrupdateReservationValidator crupdateValidator;

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
}
