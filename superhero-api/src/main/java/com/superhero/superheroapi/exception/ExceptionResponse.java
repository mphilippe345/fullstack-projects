package com.superhero.superheroapi.exception;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ExceptionResponse {

  private int code;
  private String status;
  private String message;
  private String error;
  private String path;
  private Timestamp timestamp;

}
