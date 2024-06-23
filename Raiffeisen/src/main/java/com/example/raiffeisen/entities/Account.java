package com.example.raiffeisen.entities;

import com.example.raiffeisen.models.AccountDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

  @Id
  @GeneratedValue
  private Integer id;

  private String bankName;

  private Double balance;

  private Integer userId;

  public AccountDto toDto() {
    return new AccountDto(id, bankName, balance, userId);
  }

}
