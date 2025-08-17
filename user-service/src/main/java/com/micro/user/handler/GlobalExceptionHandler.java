package com.micro.user.handler;

import com.micro.user.constant.ErrorCode;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends DataFetcherExceptionResolverAdapter {
  // Locale: messages.properties file
  private final MessageSource messageSource;

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    if (ex instanceof AppException) {
      return handleAppException((AppException) ex, env);
    } else {
      return handleGenericException(ex, env);
    }
  }

  private GraphQLError handleAppException(AppException exception, DataFetchingEnvironment env) {
    log.error("AppException occurred: {}", exception.getMessage(), exception);

    Locale lang = getLocaleFromRequest();
    ErrorCode errorCode = exception.getErrorCode();
    String localizedMessage = getLocalizedMessage(errorCode, lang);
    return GraphqlErrorBuilder.newError()
        .message(localizedMessage)
        .location(env.getField().getSourceLocation())
        .path(env.getExecutionStepInfo().getPath())
        .extensions(
            Map.of(
                "shortCode", errorCode.getShortCode(),
                "timestamp", java.time.Instant.now().toString(),
                "errorType", errorCode.getErrorType()))
        .build();
  }

  private GraphQLError handleGenericException(Throwable ex, DataFetchingEnvironment env) {
    log.error("An unexpected error occurred: {}", ex.getMessage(), ex);

    Locale lang = getLocaleFromRequest();
    ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
    String localizedMessage = getLocalizedMessage(errorCode, lang);
    return GraphqlErrorBuilder.newError()
        .message(localizedMessage)
        .location(env.getField().getSourceLocation())
        .path(env.getExecutionStepInfo().getPath())
        .extensions(
            Map.of(
                "shortCode", errorCode.getShortCode(),
                "timestamp", java.time.Instant.now().toString(),
                "errorType", errorCode.getErrorType(),
                "detailMessage", ex.getMessage()))
        .build();
  }

  /** Get locale from request header */
  private Locale getLocaleFromRequest() {
    try {
      ServletRequestAttributes attributes =
          (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attributes != null) {
        HttpServletRequest request = attributes.getRequest();
        String language = request.getHeader("Accept-Language");
        Locale locale = (language != null) ? Locale.forLanguageTag(language) : Locale.getDefault();
        log.info("Resolved locale: {}", locale);
        return locale;
      }
    } catch (Exception ex) {
      log.error("Error getting locale from request: {}", ex.getMessage(), ex);
    }
    return Locale.getDefault();
  }

  /** Get localized message from MessageSource */
  private String getLocalizedMessage(ErrorCode errorCode, Locale locale) {
    try {
      return messageSource.getMessage(errorCode.getShortCode(), null, locale);
    } catch (Exception e) {
      log.debug("Could not get localized message for {}, using default", errorCode.name(), e);
      return errorCode.getMessage();
    }
  }
}
