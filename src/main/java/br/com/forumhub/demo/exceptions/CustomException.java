package br.com.forumhub.demo.exceptions;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {
  private final String errorCode;

  public CustomException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

}
