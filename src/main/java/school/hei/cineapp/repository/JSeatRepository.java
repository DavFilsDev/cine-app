package school.hei.cineapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.cineapp.repository.model.JSeat;

public interface JSeatRepository extends JpaRepository<JSeat, String> {
  List<JSeat> findAllByRoomId(String roomId);
}
