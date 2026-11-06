package spirin.dev.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import spirin.dev.model.Account;
import spirin.dev.model.User;

import java.util.*;

@Service
@PropertySource("classpath:application.properties")
public class UserService {
    private Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Value("${account.default-amount}")
    int defaultAmount;

    public String addUser(String login) {
        if (users.values().stream().noneMatch(i -> i.getLogin().equals(login))) {
            id++;
            List<Account> accountList = new ArrayList<>();
            accountList.add(accountService.createAccount(id, defaultAmount));
            User user = new User(id, login, accountList);
            users.put(id, user);
            return user.toString();
        } else {
            throw new IllegalArgumentException("Error: A user with the login '" + login + "' already exists.");
        }
    }

    public void showAllUsers() {
        users.values().forEach(System.out::println);
    }

    public String createAccountForUser(Long id) {
        if (users.values().stream().anyMatch(i -> i.getId().equals(id))) {
            User user = users.get(id);
            Account acc = accountService.createAccount(id, 0);
            user.getAccountList().add(acc);
            return acc.toString();
        } else {
            throw new NoSuchElementException("Error: User with id '" + id + "' not found.");
        }
    }

    public String accountCloseForUser(Long accId) {
        Account acc = accountService.accountClose(accId);
        int money = acc.getMoneyAmount();
        users.get(acc.getUserId())
                .getAccountList()
                .removeIf(i -> i.getId().equals(acc.getId()));
        return "Account " + accId +
                " closed. Remaining balance " + money +
                " has been transferred to another account." + "\n";
    }
}
