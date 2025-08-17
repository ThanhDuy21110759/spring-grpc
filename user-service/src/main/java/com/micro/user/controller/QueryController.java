package com.micro.user.controller;

import com.micro.user.dto.UserResponse;
import com.micro.user.service.UserService;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class QueryController {
  private UserService userService;

  public QueryController(UserService userService) {
    this.userService = userService;
  }

  @QueryMapping
  public String hello() {
    return "Hello, GraphQL!";
  }

  @QueryMapping
  public String goodbye() {
    return "Goodbye, GraphQL!";
  }

  @QueryMapping
  public UserResponse getProfile(@Argument UUID userId) {
    return userService.getProfile(userId);
  }
}
