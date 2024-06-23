package com.example.raiffeisen;


import com.example.raiffeisen.entities.Account;
import com.example.raiffeisen.exceptions.AccountAcessDeniedException;
import com.example.raiffeisen.exceptions.AccountNotFoundException;
import com.example.raiffeisen.exceptions.InsufficentFundsException;
import com.example.raiffeisen.models.AccountDto;
import com.example.raiffeisen.repositories.AccountRepository;
import com.example.raiffeisen.security.user.Role;
import com.example.raiffeisen.security.user.User;
import com.example.raiffeisen.services.AccountServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class MockitoTests {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountServiceImpl service;

  private static final double epsilon = 0.00001d;

  @BeforeEach
  public void setUp(){
    User user = User.builder().id(1).role(Role.USER).username("test").password("test").build();
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @AfterEach
  public void tearDown(){
    SecurityContextHolder.clearContext();
  }

  @Test
  public void testCreateAccount_SuccessfulCreate() {
    String bankName = "Test Bank";
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(0.0)
        .build();

    Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

    AccountDto createdAccount = service.createAccount(bankName);


    Assertions.assertEquals(bankName, createdAccount.getBankName());
    Assertions.assertEquals(0.0, createdAccount.getBalance(), epsilon);
    Assertions.assertEquals(1, createdAccount.getUserId().intValue());

    Mockito.verify(accountRepository).save(account);
  }

  @Test
  public void testUpdateBalance_ValidUpdate() {
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(account));
    Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

    Double sum = 20.0;
    AccountDto accountDto = service.updateBalance(1, sum);

    Assertions.assertEquals(account.getId(), accountDto.getId());
    Assertions.assertEquals(balance + sum, accountDto.getBalance(), epsilon);

    Mockito.verify(accountRepository).save(account);
  }

  @Test()
  public void testUpdateBalance_AccountNotFound() {
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(balance)
        .build();

    Double sum = 20.0;

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.empty());

    Assertions.assertThrows(AccountNotFoundException.class, () -> service.updateBalance(1, sum));
  }

  @Test
  public void testUpdateBalance_AccountDenied(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(2)
        .bankName(bankName)
        .balance(balance)
        .build();

    Double sum = 20.0;

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(account));

    Assertions.assertThrows(AccountAcessDeniedException.class, () -> service.updateBalance(1, sum));
  }

  @Test
  public void testUpdateBalance_InsufficentFunds(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(account));
    Double sum = -200.0;


    Assertions.assertThrows(InsufficentFundsException.class, () -> service.updateBalance(1, sum));

  }

  @Test
  public void testFindAccount_AccountFound(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(account));

    AccountDto accountDto = service.findAccount(1);
    Assertions.assertEquals(account.getId(), accountDto.getId());
    Assertions.assertEquals(balance, accountDto.getBalance(), epsilon);
    Mockito.verify(accountRepository).findById(1);

  }

  @Test
  public void testFindAccount_AccountNotFound(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.empty());

    Assertions.assertThrows(AccountNotFoundException.class, () -> service.findAccount(1));
  }


  @Test
  public void testFindAccount_AccountDenied(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(2)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(account));

    Assertions.assertThrows(AccountAcessDeniedException.class, () -> service.findAccount(1));
  }

  @Test
  public void testDeleteAccount_DeleteSuccessful(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(account));
    Mockito.doNothing().when(accountRepository).delete(account);

    Assertions.assertAll(() -> service.deleteAccount(1));
  }

  @Test
  public void testDeleteAccount_AccountNotFound(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(1)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.empty());

    Assertions.assertThrows(AccountNotFoundException.class, () -> service.deleteAccount(1));
  }

  @Test
  public void testDeleteAccount_AccountDenied(){
    String bankName = "Test Bank";
    Double balance = 80.0;
    Account account = Account.builder()
        .userId(2)
        .bankName(bankName)
        .balance(balance)
        .build();

    Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(account));

    Assertions.assertThrows(AccountAcessDeniedException.class, () -> service.deleteAccount(1));
  }


}
