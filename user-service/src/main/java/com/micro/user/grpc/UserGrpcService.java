package com.micro.user.grpc;

import com.example.comon.constant.ErrorCode;
import com.google.protobuf.StringValue;
import com.micro.user.handler.AppException;
import com.micro.user.mapper.GrpcExceptionMapper;
import com.micro.user.mapper.UserMapper;
import com.micro.user.service.UserService;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {
  private final UserService userService;
  private final UserMapper userMapper;
  private final GrpcExceptionMapper exceptionMapper;

  @Override
  public void getProfile(StringValue request, StreamObserver<UserResponse> responseObserver) {
    try {
      String userId = request.getValue();
      com.micro.user.dto.UserResponse response = userService.getProfile(UUID.fromString(userId));
      UserResponse grpcResponse = userMapper.toGrpc(response);
      responseObserver.onNext(grpcResponse);
      responseObserver.onCompleted();
    } catch (IllegalArgumentException e) {
      // Handle UUID parsing error
      StatusException statusException =
          exceptionMapper.mapToStatusException(new AppException(ErrorCode.INVALID_INPUT));
      responseObserver.onError(statusException);

    } catch (AppException e) {
      // Handle custom app exceptions
      StatusException statusException = exceptionMapper.mapToStatusException(e);
      responseObserver.onError(statusException);

    } catch (Exception e) {
      // Handle all other exceptions
      StatusException statusException = exceptionMapper.mapToStatusException(e);
      responseObserver.onError(statusException);
    }
  }
}
