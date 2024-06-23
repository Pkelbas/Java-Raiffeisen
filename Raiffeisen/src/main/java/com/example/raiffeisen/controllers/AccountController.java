package com.example.raiffeisen.controllers;


import com.example.raiffeisen.abstractions.AccountService;
import com.example.raiffeisen.models.AccountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Счета")
public class AccountController {

  private final AccountService service;

  @Autowired
  public AccountController(AccountService accountService) {
    this.service = accountService;
  }

  @PostMapping("/add")
  @Operation(summary = "Открыть новый счет")
  public AccountDto addAccount(@RequestParam String bankName) {
    return service.createAccount(bankName);
  }

  @PutMapping("/addMoney")
  @Operation(summary = "Положить деньги на счет", description = "Сумма должна быть положительной")
  public AccountDto addMoney(@RequestParam Integer accountId, @RequestParam Double sum) {
    return service.updateBalance(accountId, sum);
  }

  @PutMapping("/withdrawMoney")
  @Operation(summary = "Снять деньги со счета", description = "Сумма должна быть положительной")
  public AccountDto withdrawMoney(@RequestParam Integer accountId, @RequestParam Double sum) {
    return service.updateBalance(accountId, -sum);
  }

  @GetMapping("/find")
  @Operation(summary = "Получить информацию по конкретному счету")
  public AccountDto findAccount(@RequestParam Integer accountId) {
    return service.findAccount(accountId);
  }

  @DeleteMapping("/close")
  @Operation(summary = "Закрыть счет")
  public void closeAccount(@RequestParam Integer accountId) {
    service.deleteAccount(accountId);
  }

  @GetMapping("/all")
  @Operation(summary = "Вывести список всех счетов принадлежащих клиенту")
  public List<AccountDto> getAllAccounts() {
    return service.getAllAccounts();
  }

  @GetMapping("/allNonZero")
  @Operation(summary = "Вывести списков счетов принадлежащих клиенту с балансом больше нуля")
  public List<AccountDto> getAllAccountsWithBalanceGreaterThanZero() {
    return service.getAllAccountsWithBalanceGreaterThanZero();
  }

  @GetMapping("/allDistinctBank")
  @Operation(summary = "Вывести списко счетов принадлежащих клиенту в определенном банке")
  public List<AccountDto> getAllAccountsWithBank(String bankName) {
    return service.getAllAccountsWithBank(bankName);
  }
}
