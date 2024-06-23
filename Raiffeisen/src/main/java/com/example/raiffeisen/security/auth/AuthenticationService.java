package com.example.raiffeisen.security.auth;


import com.example.raiffeisen.security.auth.model.Request;
import com.example.raiffeisen.security.auth.model.Response;
import com.example.raiffeisen.security.jwt.JwtService;
import com.example.raiffeisen.security.user.Role;
import com.example.raiffeisen.security.user.User;
import com.example.raiffeisen.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final PasswordEncoder encoder;
  private final AuthenticationManager manager;
  private final JwtService service;

  public Response register(Request request) {
    var user = User.builder()
        .username(request.getUsername())
        .password(encoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    repository.save(user);
    var jwtToken = service.generateToken(user);
    return Response.builder().token(jwtToken).build();
  }

  public Response authenticate(Request request) {
    manager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    var user = repository.findByUsername(request.getUsername()).orElseThrow();
    var jwtToken = service.generateToken(user);
    return Response.builder().token(jwtToken).build();
  }
}