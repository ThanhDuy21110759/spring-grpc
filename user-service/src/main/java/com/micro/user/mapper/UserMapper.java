package com.micro.user.mapper;

import com.micro.user.grpc.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMapper {
  private final ModelMapper modelMapper = new ModelMapper();

  public <D, T> D map(T entity, Class<D> outClass) {
    return modelMapper.map(entity, outClass);
  }

  public UserResponse toGrpc(com.micro.user.dto.UserResponse dto) {
    return UserResponse.newBuilder()
        .setId(dto.getId().toString())
        .setUsername(dto.getUsername())
        .setEmail(dto.getEmail())
        .setFirstName(dto.getFirstName())
        .setLastName(dto.getLastName())
        .setDob(dto.getDob())
        .setCity(dto.getCity())
        .build();
  }
}
