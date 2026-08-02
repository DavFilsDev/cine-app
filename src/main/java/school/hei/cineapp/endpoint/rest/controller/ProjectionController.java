package school.hei.cineapp.endpoint.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.cineapp.model.Projection;
import school.hei.cineapp.service.ProjectionService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class ProjectionController {
  private final ProjectionService service;

  @GetMapping("/projections")
  public List<Projection> getAllProjections() {
    return service.findAll();
  }

  @PutMapping("/projection")
  public Projection crupdateProjection(@RequestBody Projection toSave) {
    return service.save(toSave);
  }
}
