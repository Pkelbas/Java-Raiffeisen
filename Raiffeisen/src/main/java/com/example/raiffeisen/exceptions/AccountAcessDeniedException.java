package com.example.raiffeisen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountAcessDeniedException extends RuntimeException {

  public AccountAcessDeniedException(String message) {
    super(message);
  }

}
