package com.example.raiffeisen.abstractions;

import com.example.raiffeisen.models.AccountDto;
import java.util.List;

public interface AccountService {

  AccountDto createAccount(String bankName);

  AccountDto updateBalance(Integer accountId, Double sum);

  AccountDto findAccount(Integer accountId);

  void deleteAccount(Integer accountId);

  List<AccountDto> getAllAccounts();

  List<AccountDto> getAllAccountsWithBalanceGreaterThanZero();

  List<AccountDto> getAllAccountsWithBank(String bankName);

}
