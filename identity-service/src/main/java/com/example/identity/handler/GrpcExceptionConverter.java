package com.example.identity.handler;

import com.example.comon.constant.ErrorCode;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrpcExceptionConverter {

  public AppException convertToAppException(StatusRuntimeException statusException) {
    log.error(
        "Converting gRPC StatusRuntimeException to AppException: {}",
        statusException.getMessage(),
        statusException);

    // Extract metadata from gRPC exception
    Metadata trailers = statusException.getTrailers();
    if (trailers != null) {
      String errorCodeStr =
          trailers.get(Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER));
      String errorType =
          trailers.get(Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER));

      if (errorCodeStr != null) {
        ErrorCode errorCode = findErrorCodeByShortCode(errorCodeStr);
        if (errorCode != null) {
          return new AppException(errorCode);
        }
      }
    }

    // Fallback: map gRPC status code to appropriate ErrorCode
    return mapGrpcStatusToAppException(
        statusException.getStatus().getCode(), statusException.getMessage());
  }

  private ErrorCode findErrorCodeByShortCode(String shortCode) {
    for (ErrorCode errorCode : ErrorCode.values()) {
      if (errorCode.getShortCode().equals(shortCode)) {
        return errorCode;
      }
    }
    return null;
  }

  private AppException mapGrpcStatusToAppException(Status.Code grpcStatusCode, String message) {
    return switch (grpcStatusCode) {
      case INVALID_ARGUMENT -> new AppException(ErrorCode.INVALID_INPUT);
      case NOT_FOUND -> new AppException(ErrorCode.USER_NOT_FOUND);
      case ALREADY_EXISTS -> new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
      case UNAUTHENTICATED -> new AppException(ErrorCode.AUTHENTICATION_FAILED);
      case PERMISSION_DENIED -> new AppException(ErrorCode.ACCESS_DENIED);
      case INTERNAL, UNKNOWN -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
      default -> {
        log.warn("Unmapped gRPC status code: {}, message: {}", grpcStatusCode, message);
        yield new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
      }
    };
  }
}
