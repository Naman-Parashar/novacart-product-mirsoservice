package com.test.product_service.exception.custom_exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException  extends RuntimeException {
  private final String errorMsg;

  public ResourceNotFoundException (String message, String errorMsg) {
    super(message);
    this.errorMsg = errorMsg;
  }
}
