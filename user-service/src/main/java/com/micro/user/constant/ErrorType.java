package com.micro.user.constant;

import lombok.Getter;

@Getter
public enum ErrorType {
  INTERNAL_ERROR,
  BAD_REQUEST,
  UNAUTHORIZED,
  FORBIDDEN,
  NOT_FOUND,
  CONFLICT,
}
