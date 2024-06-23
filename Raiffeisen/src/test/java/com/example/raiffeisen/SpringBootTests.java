package com.example.raiffeisen;

import com.example.raiffeisen.abstractions.AccountService;
import com.example.raiffeisen.exceptions.AccountAccessDeniedException;
import com.example.raiffeisen.exceptions.AccountNotFoundException;
import com.example.raiffeisen.exceptions.InsufficientFundsException;
import com.example.raiffeisen.models.AccountDto;
import com.example.raiffeisen.security.user.Role;
import com.example.raiffeisen.security.user.User;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class SpringBootTests {

  @Autowired
  private AccountService service;

  private static final double epsilon = 0.00001d;

  @BeforeEach
  public void setUp() {
    User user = User.builder().id(999).role(Role.USER).username("test").password("test").build();
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }


  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }


  @Test
  @Transactional
  public void testCreateAccount_SuccessfulCreate() {
    String bankName = "Test Bank";

    AccountDto createdAccount = service.createAccount(bankName);

    Assertions.assertEquals(bankName, createdAccount.getBankName());
    Assertions.assertEquals(0.0, createdAccount.getBalance(), epsilon);
    Assertions.assertEquals(999, createdAccount.getUserId().intValue());
  }

  @Test
  @Transactional
  public void testUpdateBalance_ValidUpdate() {
    String bankName = "Test Bank";

    Double sum = 20.0;
    AccountDto accountDto = service.createAccount(bankName);

    AccountDto updatedAccountDto = service.updateBalance(accountDto.getId(), sum);

    Assertions.assertEquals(accountDto.getId(), updatedAccountDto.getId());
    Assertions.assertEquals(accountDto.getBalance() + sum, updatedAccountDto.getBalance(), epsilon);
  }

  @Test()
  @Transactional
  public void testUpdateBalance_AccountNotFound() {

    Double sum = 20.0;

    Assertions.assertThrows(AccountNotFoundException.class, () -> service.updateBalance(1000, sum));
  }

  @Test
  @Transactional
  public void testUpdateBalance_AccountDenied() {
    String bankName = "Test Bank";

    AccountDto accountDto = service.createAccount(bankName);

    User user = User.builder().id(1000).role(Role.USER).username("test").password("test").build();
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Double sum = 20.0;

    Assertions.assertThrows(AccountAccessDeniedException.class, () -> service.updateBalance(
        accountDto.getId(), sum));
  }

  @Test
  @Transactional
  public void testUpdateBalance_InsufficientFunds() {
    String bankName = "Test Bank";

    Double sum = -20.0;
    AccountDto accountDto = service.createAccount(bankName);

    Assertions.assertThrows(InsufficientFundsException.class, () -> service.updateBalance(
        accountDto.getId(), sum));

  }

  @Test
  @Transactional
  public void testFindAccount_AccountFound() {
    String bankName = "Test Bank";

    AccountDto accountDto = service.createAccount(bankName);

    AccountDto updatedAccountDto = service.findAccount(accountDto.getId());

    Assertions.assertEquals(accountDto, updatedAccountDto);
  }

  @Test
  @Transactional
  public void testFindAccount_AccountNotFound() {

    Assertions.assertThrows(AccountNotFoundException.class, () -> service.findAccount(1000));
  }


  @Test
  @Transactional
  public void testFindAccount_AccountDenied() {
    String bankName = "Test Bank";

    AccountDto accountDto = service.createAccount(bankName);

    User user = User.builder().id(6).role(Role.USER).username("test").password("test").build();
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Assertions.assertThrows(AccountAccessDeniedException.class,
        () -> service.findAccount(accountDto.getId()));
  }

  @Test
  @Transactional
  public void testDeleteAccount_DeleteSuccessful() {
    String bankName = "Test Bank";

    AccountDto accountDto = service.createAccount(bankName);

    service.deleteAccount(accountDto.getId());

    Assertions.assertThrows(AccountNotFoundException.class,
        () -> service.findAccount(accountDto.getId()));
  }

  @Test
  @Transactional
  public void testDeleteAccount_AccountNotFound() {
    Assertions.assertThrows(AccountNotFoundException.class, () -> service.deleteAccount(1000));
  }

  @Test
  @Transactional
  public void testDeleteAccount_AccountDenied() {
    String bankName = "Test Bank";

    AccountDto accountDto = service.createAccount(bankName);

    User user = User.builder().id(6).role(Role.USER).username("test").password("test").build();
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Assertions.assertThrows(AccountAccessDeniedException.class, () -> service.deleteAccount(
        accountDto.getId()));
  }

  @Test
  @Transactional
  public void testGetAllAccounts_Successful() {
    String bankName = "Test Bank";
    AccountDto accountDto = service.createAccount(bankName);
    AccountDto accountDto2 = service.createAccount(bankName);

    List<AccountDto> result = service.getAllAccounts();
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(accountDto, result.get(0));
    Assertions.assertEquals(accountDto2, result.get(1));
  }


  @Test
  @Transactional
  public void testGetAllAccountsWithBalanceGreaterThanZero_Successful() {
    String bankName = "Test Bank";
    AccountDto accountDto = service.createAccount(bankName);
    AccountDto accountDto2 = service.createAccount(bankName);

    accountDto = service.updateBalance(accountDto.getId(), 20.0);
    List<AccountDto> result = service.getAllAccountsWithBalanceGreaterThanZero();
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(accountDto, result.get(0));
  }

  @Test
  @Transactional
  public void testGetAllAccountsWithBank() {
    String bankName = "Test Bank";
    AccountDto accountDto = service.createAccount(bankName);
    AccountDto accountDto2 = service.createAccount(bankName);

    List<AccountDto> result = service.getAllAccountsWithBank(bankName);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(accountDto, result.get(0));
    Assertions.assertEquals(accountDto2, result.get(1));
  }


}
