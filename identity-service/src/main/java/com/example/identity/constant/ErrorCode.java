package com.example.identity.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // 500: INTERNAL_ERROR
  UNCATEGORIZED_EXCEPTION(
      "UNCATEGORIZED_EXCEPTION",
      "An uncategorized exception occurred. Please try again later.",
      ErrorType.INTERNAL_ERROR),
  // 400: BAD_REQUEST
  EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email already exists.", ErrorType.BAD_REQUEST),
  INVALID_INPUT("INVALID_INPUT", "Invalid input provided.", ErrorType.BAD_REQUEST),
  INVALID_UUID("INVALID_UUID", "Invalid UUID format.", ErrorType.BAD_REQUEST),
  // 401: UNAUTHORIZED
  AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", "Authentication failed.", ErrorType.UNAUTHORIZED),
  // 403: FORBIDDEN
  ACCESS_DENIED("ACCESS_DENIED", "Access denied.", ErrorType.FORBIDDEN),
  // 404: NOT_FOUND
  USER_NOT_FOUND("USER_NOT_FOUND", "User not found.", ErrorType.NOT_FOUND),
  // 409: CONFLICT
  DATA_CONFLICT("DATA_CONFLICT", "Data conflict occurred.", ErrorType.CONFLICT);

  private final String shortCode;
  private final String message;
  private final ErrorType errorType;

  ErrorCode(String shortCode, String message, ErrorType errorType) {
    this.shortCode = shortCode;
    this.message = message;
    this.errorType = errorType;
  }
}
