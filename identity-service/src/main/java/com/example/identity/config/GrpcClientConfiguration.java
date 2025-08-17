package com.example.identity.config;

import com.micro.user.grpc.UserGrpcServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfiguration {
  private final String HOST = "localhost";
  private final int PORT = 9090;

  @Bean
  UserGrpcServiceGrpc.UserGrpcServiceBlockingStub userGrpcService() {
    Channel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();
    return UserGrpcServiceGrpc.newBlockingStub(channel);
  }
}
