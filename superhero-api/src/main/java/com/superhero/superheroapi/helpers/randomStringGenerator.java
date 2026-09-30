package com.superhero.superheroapi.helpers;

import java.security.SecureRandom;

public class randomStringGenerator {

  public static String generateRandomString(int size, boolean withDash) {
    String allowedCharacters = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    SecureRandom random = new SecureRandom();
    int capacity = size + (Math.floorDiv(size, 4) - 1);
    StringBuilder codeBuilder = new StringBuilder(capacity);

    for (int i = 0; i < size; i++) {
      if (withDash) {
        if (i > 0 && i % 4 == 0) {
          codeBuilder.append('-');
        }
      }
      int randomIndex = random.nextInt(allowedCharacters.length());
      char randomChar = allowedCharacters.charAt(randomIndex);
      codeBuilder.append(randomChar);
    }

    return codeBuilder.toString();
  }
}
