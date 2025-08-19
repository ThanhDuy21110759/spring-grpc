package com.micro.user.mapper;

import com.example.comon.constant.ErrorCode;
import com.example.comon.constant.ErrorType;
import com.micro.user.handler.AppException;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrpcExceptionMapper {

  private final MessageSource messageSource;

  public GrpcExceptionMapper(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public StatusException mapToStatusException(Throwable throwable) {
    if (throwable instanceof AppException) {
      return handleAppException((AppException) throwable);
    } else {
      return handleGenericException(throwable);
    }
  }

  private StatusException handleAppException(AppException exception) {
    log.error("AppException in gRPC: {}", exception.getMessage(), exception);

    ErrorCode errorCode = exception.getErrorCode();
    Status.Code grpcStatusCode = mapErrorTypeToGrpcStatus(errorCode.getErrorType());

    // Adding metadata to the gRPC status exception
    Metadata metadata = new Metadata();
    metadata.put(
        Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER), errorCode.getShortCode());
    metadata.put(
        Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER),
        errorCode.getErrorType().name());
    metadata.put(
        Metadata.Key.of("timestamp", Metadata.ASCII_STRING_MARSHALLER),
        java.time.Instant.now().toString());

    return Status.fromCode(grpcStatusCode)
        .withDescription(exception.getMessage())
        .asException(metadata);
  }

  private StatusException handleGenericException(Throwable throwable) {
    log.error("Generic exception in gRPC: {}", throwable.getMessage(), throwable);

    return Status.INTERNAL.withDescription("An unexpected error occurred").asException();
  }

  private Status.Code mapErrorTypeToGrpcStatus(ErrorType errorType) {
    return switch (errorType) {
      case BAD_REQUEST -> Status.Code.INVALID_ARGUMENT;
      case UNAUTHORIZED -> Status.Code.UNAUTHENTICATED;
      case FORBIDDEN -> Status.Code.PERMISSION_DENIED;
      case NOT_FOUND -> Status.Code.NOT_FOUND;
      case INTERNAL_ERROR -> Status.Code.INTERNAL;
      case CONFLICT -> Status.Code.ALREADY_EXISTS;
      default -> Status.Code.UNKNOWN;
    };
  }
}
