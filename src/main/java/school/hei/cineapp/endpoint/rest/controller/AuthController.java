package school.hei.cineapp.endpoint.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.cineapp.model.LoginPayload;
import school.hei.cineapp.model.RegisterPayload;
import school.hei.cineapp.model.UserWithToken;
import school.hei.cineapp.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class AuthController {
  private final UserService service;

  @PostMapping("/register")
  public UserWithToken register(@RequestBody RegisterPayload payload) {
    return service.register(payload);
  }

  @PostMapping("/login")
  public UserWithToken login(@RequestBody LoginPayload payload) {
    return service.login(payload);
  }
}
