package com.superhero.superheroapi.domains.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmtpSecret {

  private String host;
  private String port;
  private String username;
  private String password;
}
