package com.micro.user.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private Instant dob;
  private String city;
}
