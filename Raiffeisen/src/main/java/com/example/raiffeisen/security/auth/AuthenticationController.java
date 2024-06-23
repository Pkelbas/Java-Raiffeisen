package com.example.raiffeisen.security.auth;

import com.example.raiffeisen.security.auth.model.Request;
import com.example.raiffeisen.security.auth.model.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Регистрация/Аутенфикация")
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/reg")
  @Cacheable("register")
  @Operation(summary = "Регистрация")
  public ResponseEntity<Response> register(@RequestParam String username, @RequestParam String password) {
    return ResponseEntity.ok(service.register(new Request(username, password)));
  }

  @PostMapping("/auth")
  @Cacheable("authenticate")
  @Operation(summary = "Аутенфикация")
  public ResponseEntity<Response> authenticate(@RequestParam String username, @RequestParam String password) {
    return ResponseEntity.ok(service.authenticate(new Request(username, password)));
  }


}