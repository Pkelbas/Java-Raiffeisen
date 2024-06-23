package com.example.raiffeisen.repositories;

import com.example.raiffeisen.entities.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

  List<Account> findAllByBankNameAndUserId(String bankName, Integer userId);

  List<Account> findAllByBalanceGreaterThanAndUserId(double balance, Integer userId);

  List<Account> findAllByUserId(int userId);

}
