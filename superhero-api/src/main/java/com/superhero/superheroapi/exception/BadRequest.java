package com.superhero.superheroapi.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequest extends RuntimeException {

  public BadRequest(String message) {
    super(message);
  }
}
