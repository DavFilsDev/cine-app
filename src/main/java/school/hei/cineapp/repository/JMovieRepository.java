package school.hei.cineapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.cineapp.repository.model.JMovie;

public interface JMovieRepository extends JpaRepository<JMovie, String> {}
