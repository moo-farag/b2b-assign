package com.b2b.assignment.web.rest.errors;

import java.util.AbstractMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Global Exception handler for all errors.
   */
  @ExceptionHandler
  public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception exception) {
    log.error("Exception: Unable to process this request. ", exception);
    AbstractMap.SimpleEntry<String, String> response = new AbstractMap.SimpleEntry<>("message", "Unable to process this request.");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
