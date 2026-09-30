package com.superhero.superheroapi.datasourceconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RdsDatabaseSecret {

  private String username;
  private String password;
  private String driverClassName;
  private String engine;
  private String host;
  private String port;
  private String dbname;
}
