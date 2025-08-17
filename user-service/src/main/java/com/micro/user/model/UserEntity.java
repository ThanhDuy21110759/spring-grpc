package com.micro.user.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "public", catalog = "postgres")
public class UserEntity extends Auditable implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, columnDefinition = "uuid", updatable = false)
  private UUID id;

  @Column(name = "username", nullable = true, length = 100)
  private String username;

  @Column private String email;

  @Column private String firstName;

  @Column private String lastName;

  @Column private Instant dob;

  @Column private String city;
}
