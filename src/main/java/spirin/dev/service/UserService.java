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
    private final Map<Long, User> userList = new HashMap<>();
    private final AccountService accountService;

    public UserService(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @Value("${account.default-amount}")
    int defaultAmount;

    public void addUser(String login)
    {
        if (userList.values().stream().noneMatch(i -> i.getLogin().equals(login)))
        {
            id++;
            List<Account> accountList = new ArrayList<>();
            accountList.add(accountService.createAccount(id, defaultAmount));
            User user = new User(id, login, accountList);
            userList.put(id, user);
            System.out.println("User created: " + user.toString() + "\n");
        }
        else {
            System.out.println("Error\n");
        }
    }

    public void showAllUsers()
    {
        userList.values().forEach(System.out::println);
    }

    public void createAccountForUser(Long id)
    {
        if (userList.values().stream().allMatch(i -> i.getId().equals(id)))
        {
            User user = userList.get(id);
            Account acc = accountService.createAccount(id, 0);
            user.getAccountList().add(acc);
            System.out.println("Account created: " + acc.toString() + "\n");
        }
        else
        {
            System.out.println("Error\n");
        }
    }

    public void accountCloseForUser(Long id)
    {
        Long userId = accountService.accountClose(id);
        if (userId != 0L)
        {
            userList.get(userId)
                    .getAccountList()
                    .removeIf(i -> i.getId().equals(id));
        }
    }
}
