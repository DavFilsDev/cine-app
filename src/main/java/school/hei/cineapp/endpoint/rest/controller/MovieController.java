package school.hei.cineapp.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.cineapp.model.Movie;
import school.hei.cineapp.service.MovieService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class MovieController {
  private final MovieService service;

  @GetMapping("/movies")
  public List<Movie> getAllMovies() {
    return service.findAll();
  }

  @GetMapping("/movies/{movieId}")
  public Movie getMovieById(@PathVariable String movieId) {
    return service.getById(movieId);
  }

  @PutMapping("/movies")
  public Movie crupdateMovie(@RequestBody Movie toSave) {
    return service.save(toSave);
  }
}
