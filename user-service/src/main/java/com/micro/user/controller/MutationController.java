package com.micro.user.controller;

import com.micro.user.constant.ErrorCode;
import com.micro.user.dto.UserRequest;
import com.micro.user.dto.UserResponse;
import com.micro.user.handler.AppException;
import com.micro.user.service.UserService;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MutationController {
  private UserService userService;

  public MutationController(UserService userService) {
    this.userService = userService;
  }

  @MutationMapping
  public String createUser(@Argument String name) {
    return "User " + name + " created successfully!";
  }

  @MutationMapping
  public String testGlobalException() {
    throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
  }

  @MutationMapping
  public UserResponse createProfile(@Argument("request") UserRequest request) {
    return userService.createProfile(request);
  }

  @MutationMapping
  public UserResponse updateProfile(
      @Argument("request") UserRequest request, @Argument("userId") String userId) {
    return userService.updateProfile(request, UUID.fromString(userId));
  }

  @MutationMapping
  public String deleteProfile(@Argument("userId") String userId) {
    return userService.deleteProfile(UUID.fromString(userId));
  }
}
