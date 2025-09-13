package com.example.batch;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private Integer customerId;

  private Integer itemId;

  private String itemName;

  private Integer itemPrice;

  private String purchaseDate;
}
