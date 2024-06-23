package com.example.raiffeisen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountAccessDeniedException extends RuntimeException {

  public AccountAccessDeniedException(String message) {
    super(message);
  }

}
