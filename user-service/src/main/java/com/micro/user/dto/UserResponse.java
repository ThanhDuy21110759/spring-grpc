package com.micro.user.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private UUID id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String dob;
  private String city;
}
