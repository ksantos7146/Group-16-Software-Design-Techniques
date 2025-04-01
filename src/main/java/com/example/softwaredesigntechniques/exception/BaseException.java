package com.example.softwaredesigntechniques.exception;

import lombok.Getter;

@Getter
public class BaseException extends Exception {
  private final String message;

  public BaseException(String message) {
    this.message = message;
  }
}
