package school.hei.cineapp.service;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import school.hei.cineapp.model.LoginPayload;
import school.hei.cineapp.model.RegisterPayload;
import school.hei.cineapp.model.User;
import school.hei.cineapp.model.UserWithToken;
import school.hei.cineapp.repository.JUserRepository;
import school.hei.cineapp.repository.mapper.JUserMapper;
import school.hei.cineapp.security.jwt.JwtService;
import school.hei.cineapp.security.model.Principal;
import school.hei.cineapp.service.validator.LoginUserPayloadValidator;
import school.hei.cineapp.service.validator.RegisterUserPayloadValidator;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
  private final JUserRepository jRepository;
  private final JUserMapper jMapper;
  private final RegisterUserPayloadValidator registerUserPayloadValidator;
  private final LoginUserPayloadValidator loginUserPayloadValidator;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private User getByEmail(String email) {
    return jRepository
        .findByEmail(email)
        .map(jMapper::toDomain)
        .orElseThrow(() -> new NoSuchElementException("Email not found"));
  }

  public UserWithToken register(RegisterPayload payload) {
    registerUserPayloadValidator.accept(payload);

    var toRegister = payload.toNewUser();
    toRegister.setPassword(passwordEncoder.encode(toRegister.getPassword()));

    var saved = jRepository.save(jMapper.toEntity(toRegister));
    var domain = jMapper.toDomain(saved);
    var token = jwtService.generate(new Principal(domain));
    return UserWithToken.from(domain, token);
  }

  public UserWithToken login(LoginPayload payload) {
    loginUserPayloadValidator.accept(payload);

    var principal = loadUserByUsername(payload.email());
    if (!passwordEncoder.matches(payload.password(), principal.getPassword())) {
      throw new BadCredentialsException("Invalid email or password");
    }

    var token = jwtService.generate(principal);
    return UserWithToken.from(principal.user(), token);
  }

  @Override
  public Principal loadUserByUsername(String email) throws UsernameNotFoundException {
    return new Principal(getByEmail(email));
  }
}
