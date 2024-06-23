package com.example.raiffeisen.security.user;


import static com.example.raiffeisen.security.user.Permission.USER_CREATE;
import static com.example.raiffeisen.security.user.Permission.USER_DELETE;
import static com.example.raiffeisen.security.user.Permission.USER_READ;
import static com.example.raiffeisen.security.user.Permission.USER_UPDATE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role {
  USER(
      Set.of(
          USER_READ,
          USER_CREATE,
          USER_UPDATE,
          USER_DELETE
      )
  );

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
        .stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }


  public String roleName(){
    return "ROLE_" + this.name();
  }
}