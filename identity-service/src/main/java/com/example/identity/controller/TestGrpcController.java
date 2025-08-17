package com.example.identity.controller;

import com.example.identity.handler.GrpcExceptionConverter;
import com.google.protobuf.StringValue;
import com.micro.user.grpc.UserGrpcServiceGrpc;
import com.micro.user.grpc.UserResponse;
import io.grpc.StatusRuntimeException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class TestGrpcController {
  private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub userGrpcService;
  private final GrpcExceptionConverter grpcExceptionConverter;

  public TestGrpcController(
      UserGrpcServiceGrpc.UserGrpcServiceBlockingStub userGrpcService,
      GrpcExceptionConverter grpcExceptionConverter) {
    this.userGrpcService = userGrpcService;
    this.grpcExceptionConverter = grpcExceptionConverter;
  }

  @QueryMapping
  public String helloGrpc() {
    return "Hello from gRPC!";
  }

  @QueryMapping
  public UserResponse getProfile(@Argument UUID userId) {
    try {
      return userGrpcService.getProfile(StringValue.of(userId.toString()));
    } catch (StatusRuntimeException ex) {
      throw grpcExceptionConverter.convertToAppException(ex);
    }
  }
}
