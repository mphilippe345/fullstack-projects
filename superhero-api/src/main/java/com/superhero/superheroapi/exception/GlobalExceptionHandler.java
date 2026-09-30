package com.superhero.superheroapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler for the exceptions that we are going to be using throughout the
 * application.
 *
 * @author Ricardo Reyes-Benavides
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * This method handles the NotFound exception class when it is thrown to a user. The exception is
   * thrown, displays a message to the user and sets the status to 404.
   *
   * @param e       NotFound Exception that is thrown.
   * @param request Request that was made. Should be left empty when exception is thrown.
   * @return ExceptionResponse class with all information for a user, status set to 404.
   */

  @ExceptionHandler(NotFound.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ExceptionResponse> notFound(NotFound e, HttpServletRequest request) {
    ExceptionResponse response = ExceptionResponse.builder()
        .code(HttpStatus.NOT_FOUND.value())
        .status(HttpStatus.NOT_FOUND.toString())
        .error("The requested resource was not found.")
        .message(e.getMessage())
        .path(request.getRequestURI())
        .timestamp(Timestamp.from(Instant.now()))
        .build();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  /**
   * This method handles the Server exception class when it is thrown to a user. The exception is
   * thrown, displays a message to the user and sets the status to 500. This method is for the
   * purposes of throwing a 500 status with a custom message.
   *
   * @param e       ServerError Exception that is thrown.
   * @param request Request that was made. Should be left empty when exception is thrown.
   * @return ExceptionResponse class with all information for a user, status set to 500.
   */
  @ExceptionHandler(ServerError.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ExceptionResponse> serverError(ServerError e, HttpServletRequest request) {
    ExceptionResponse response = ExceptionResponse.builder()
        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
        .error("There was an error in the server, please try again later.")
        .message(e.getMessage())
        .timestamp(Timestamp.from(Instant.now()))
        .path(request.getRequestURI())
        .build();
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * This method handles the response from the server for any uncaught exceptions. The exception is
   * thrown, displays a message to the user and sets the status to 500.
   *
   * @param e       Exception that is thrown.
   * @param request Request that was made. Should be left empty when exception is thrown.
   * @return ExceptionResponse class with all information for a user, status set to 500.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ExceptionResponse> genericError(Exception e, HttpServletRequest request) {
    ExceptionResponse response = ExceptionResponse.builder()
        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
        .error(e.toString())
        .message("A server error has occurred.")
        .timestamp(Timestamp.from(Instant.now()))
        .path(request.getRequestURI())
        .build();
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BadRequest.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExceptionResponse> badRequest(BadRequest e, HttpServletRequest request) {
    ExceptionResponse response =
        ExceptionResponse.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .status(HttpStatus.BAD_REQUEST.toString())
            .message("An error occurred. One or more values is invalid.")
            .error(e.toString())
            .path(request.getRequestURI())
            .timestamp(Timestamp.from(Instant.now()))
            .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * This method handles the Conflict exception class when it is thrown to a user. The exception is
   * thrown, displays a message to the user and sets the status to 409.
   *
   * @param e       Conflict Exception that is thrown.
   * @param request Request that was made. Should be left empty when exception is thrown.
   * @return ExceptionResponse class with all information for a user, status set to 409.
   */
  @ExceptionHandler(Conflict.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ExceptionResponse> conflict(Conflict e, HttpServletRequest request) {
    ExceptionResponse response = ExceptionResponse.builder()
        .code(HttpStatus.CONFLICT.value())
        .status(HttpStatus.CONFLICT.toString())
        .error("A conflict occurred when attempting to process the request.")
        .message(e.getMessage())
        .timestamp(Timestamp.from(Instant.now()))
        .path(request.getRequestURI())
        .build();
    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }

  /**
   * Exception handler for MethodArgumentNotValidException, which occurs when validation of method
   * arguments fails. Handles the exception and returns a map of field names and corresponding error
   * messages.
   *
   * @param ex The MethodArgumentNotValidException that occurred.
   * @return A map of field names and corresponding error messages.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

}
