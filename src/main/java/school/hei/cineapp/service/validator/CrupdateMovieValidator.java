package school.hei.cineapp.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import school.hei.cineapp.model.Movie;

@Component
public class CrupdateMovieValidator implements Consumer<Movie> {
  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(Movie movie) {
    var sj = new StringJoiner(". ");

    if (isBlank(movie.id())) {
      sj.add("Id is mandatory");
    }
    if (isBlank(movie.title())) {
      sj.add("Title is mandatory");
    }
    if (movie.genre() == null || movie.genre().isEmpty()) {
      sj.add("At least one genre is mandatory");
    }
    if (movie.duration() == null || movie.duration().isZero() || movie.duration().isNegative()) {
      sj.add("Duration must be strictly positive");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
