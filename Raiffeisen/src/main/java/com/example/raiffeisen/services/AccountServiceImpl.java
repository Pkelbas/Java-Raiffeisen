package com.example.raiffeisen.services;

import com.example.raiffeisen.abstractions.AccountService;
import com.example.raiffeisen.entities.Account;
import com.example.raiffeisen.exceptions.AccountAccessDeniedException;
import com.example.raiffeisen.exceptions.AccountNotFoundException;
import com.example.raiffeisen.exceptions.InsufficientFundsException;
import com.example.raiffeisen.models.AccountDto;
import com.example.raiffeisen.repositories.AccountRepository;
import com.example.raiffeisen.security.user.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

  private final double epsilon = 0.00001d;

  private final AccountRepository accountRepository;

  @Autowired
  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public AccountDto createAccount(String bankName) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer id = user.getId();
    Account account = new Account();
    account.setUserId(id);
    account.setBankName(bankName);
    account.setBalance(0.0);
    return accountRepository.save(account).toDto();
  }

  public AccountDto updateBalance(Integer accountId, Double sum) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer id = user.getId();
    Account account = accountRepository.findById(accountId)
        .orElseThrow(
            () -> new AccountNotFoundException("No account by this account id: " + accountId.toString()));
    if (!account.getUserId().equals(id)) {
      throw new AccountAccessDeniedException(
          "This user: " + user.getUsername() + " lacks the right to use account with id: " + accountId.toString());
    }

    if (account.getBalance() + sum < epsilon) {
      throw new InsufficientFundsException(
          "Balance in account: " + accountId.toString() + " insufficient to make a withdrawal of sum: "
              + sum.toString());
    }
    account.setBalance(account.getBalance() + sum);
    return accountRepository.save(account).toDto();
  }

  public AccountDto findAccount(Integer accountId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer id = user.getId();
    Account account = accountRepository.findById(accountId)
        .orElseThrow(
            () -> new AccountNotFoundException("No account by this account id: " + accountId.toString()));
    if (!account.getUserId().equals(id)) {
      throw new AccountAccessDeniedException(
          "This user: " + user.getUsername() + " lacks the right to use account with id: " + accountId.toString());
    }
    return account.toDto();
  }

  public void deleteAccount(Integer accountId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer id = user.getId();
    Account account = accountRepository.findById(accountId)
        .orElseThrow(
            () -> new AccountNotFoundException("No account by this account id: " + accountId.toString()));
    if (!account.getUserId().equals(id)) {
      throw new AccountAccessDeniedException(
          "This user: " + user.getUsername() + " lacks the right to use account with id: " + accountId.toString());
    }
    accountRepository.delete(account);
  }


  public List<AccountDto> getAllAccounts() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer id = user.getId();
    return accountRepository.findAllByUserId(id).stream().map(Account::toDto).toList();
  }

  public List<AccountDto> getAllAccountsWithBalanceGreaterThanZero() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer id = user.getId();
    return accountRepository.findAllByBalanceGreaterThanAndUserId(epsilon, id).stream()
        .map(Account::toDto).toList();
  }

  public List<AccountDto> getAllAccountsWithBank(String bankName) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer id = user.getId();
    return accountRepository.findAllByBankNameAndUserId(bankName, id).stream().map(Account::toDto)
        .toList();
  }
}
