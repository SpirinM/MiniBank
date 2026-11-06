package spirin.dev.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import spirin.dev.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@PropertySource("classpath:application.properties")
public class AccountService {
    private Long id = 0L;
    private final Map<Long, Account> accounts = new HashMap<>();
    @Value("${account.transfer-commission}")
    double commission;

    public Account createAccount(Long userId, int moneyAmount) {
        id++;
        Account account = new Account(id, userId, moneyAmount);
        accounts.put(id, account);
        return account;
    }

    public int accountDeposit(Long id, int moneyAmount) {
        if (moneyAmount <= 0) {
            throw new IllegalArgumentException("Error: The deposit amount cannot be equal to " + moneyAmount + ".");
        }
        if (accounts.values().stream().anyMatch(i -> i.getId().equals(id))) {
            int money = accounts.get(id).getMoneyAmount() + moneyAmount;
            accounts.get(id).setMoneyAmount(money);
            return money;
        } else {
            throw new NoSuchElementException("Error: Account with id '" + id + "' not found.");
        }
    }

    public int accountWithdraw(Long id, int moneyAmount) {
        if (moneyAmount <= 0) {
            throw new IllegalArgumentException("Error: The withdrawal amount cannot be equal to " + moneyAmount + ".");
        }
        if (accounts.values().stream().anyMatch(i -> i.getId().equals(id))) {
            int money = accounts.get(id).getMoneyAmount() - moneyAmount;
            if (money < 0) {
                throw new IllegalArgumentException("Error: insufficient funds on account id=" +
                        id + " moneyAmount=" +
                        accounts.get(id).getMoneyAmount() +
                        ", attempted withdraw=" + moneyAmount + "\n");
            } else {
                accounts.get(id).setMoneyAmount(money);
                return money;
            }
        } else {
            throw new NoSuchElementException("Error: Account with id '" + id + "' not found.");
        }
    }

    public int accountTransfer(Long initialId, Long finalId, int moneyAmount) {
        if (!accounts.containsKey(initialId)) {
            throw new NoSuchElementException("Error: Account with id '" + initialId + "' not found.");
        }
        if (!accounts.containsKey(finalId)) {
            throw new NoSuchElementException("Error: Account with id '" + finalId + "' not found.");
        }
        if (moneyAmount <= 0) {
            throw new IllegalArgumentException("Error: The withdrawal amount cannot be equal to " + moneyAmount + ".");
        }

        Account initialAcc = accounts.get(initialId);
        Account finalAcc = accounts.get(finalId);

        int initialMoneyAmount = initialAcc.getMoneyAmount() - moneyAmount;
        if (initialMoneyAmount < 0) {
            throw new IllegalArgumentException("Error: insufficient funds on account id=" +
                    initialId + " moneyAmount=" +
                    initialAcc.getMoneyAmount() +
                    ", attempted transfer=" + moneyAmount + "\n");
        }

        initialAcc.setMoneyAmount(initialMoneyAmount);
        int finalMoneyAmount;
        int commis = 0;
        if (!Objects.equals(initialAcc.getUserId(), finalAcc.getUserId())) {
            commis = (int) (moneyAmount - (moneyAmount * (1 - commission)));
            finalMoneyAmount = finalAcc.getMoneyAmount() + moneyAmount - commis;
        } else {
            finalMoneyAmount = finalAcc.getMoneyAmount() + moneyAmount;
        }
        finalAcc.setMoneyAmount(finalMoneyAmount);
        return commis;
    }

    public Account accountClose(Long id) {
        if (!accounts.containsKey(id)) {
            throw new NoSuchElementException("Error: Account with id '" + id + "' not found.");
        }

        Long userId = accounts.get(id).getUserId();
        int money = accounts.get(id).getMoneyAmount();

        Account secondAcc = accounts.values().stream()
                .filter(i -> i.getUserId().equals(userId))
                .filter(i -> !Objects.equals(i.getId(), id))
                .findFirst().orElseThrow(() ->
                        new IllegalArgumentException(
                                "Error: You cannot close an account if the total number of accounts is <= 1."));
        secondAcc.setMoneyAmount(secondAcc.getMoneyAmount() + money);

        Account acc = accounts.get(id);
        accounts.remove(id);
        return acc;
    }
}
