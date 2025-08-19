package com.micro.user.service;

import com.example.comon.constant.ErrorCode;
import com.micro.user.dto.UserRequest;
import com.micro.user.dto.UserResponse;
import com.micro.user.handler.AppException;
import com.micro.user.mapper.UserMapper;
import com.micro.user.model.UserEntity;
import com.micro.user.repository.UserRepository;
import com.micro.user.utils.NullPropertyUtils;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private UserRepository userRepository;
  private UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserResponse getProfile(UUID userId) {
    return userRepository
        .findById(userId)
        .map(user -> userMapper.map(user, UserResponse.class))
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
  }

  public UserResponse createProfile(UserRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    UserEntity userEntity = userMapper.map(request, UserEntity.class);
    userEntity = userRepository.save(userEntity);
    return userMapper.map(userEntity, UserResponse.class);
  }

  public UserResponse updateProfile(UserRequest request, UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    BeanUtils.copyProperties(request, userEntity, NullPropertyUtils.getNullPropertyNames(request));
    userEntity = userRepository.save(userEntity);
    return userMapper.map(userEntity, UserResponse.class);
  }

  public String deleteProfile(UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    userRepository.delete(userEntity);
    return "DELETE_PROFILE_SUCCESS";
  }
}
