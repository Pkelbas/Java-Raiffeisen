package com.example.raiffeisen.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AccountDto {

  private Integer id;

  private String bankName;

  private Double balance;

  private Integer userId;
}
