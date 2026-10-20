package spirin.dev.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import spirin.dev.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@PropertySource("classpath:application.properties")
public class AccountService {
    private Long id = 0L;
    private final Map<Long, Account> accountList = new HashMap<>();
    @Value("${account.transfer-commission}")
    double commission;

    public Account createAccount(Long userId, int moneyAmount)
    {
        id++;
        Account account = new Account(id, userId, moneyAmount);
        accountList.put(id, account);
        return account;
    }

    public void accountDeposit(Long id, int moneyAmount)
    {
        if ((accountList.values().stream().allMatch(i -> i.getId().equals(id))) && moneyAmount > 0)
        {
            int money = accountList.get(id).getMoneyAmount() + moneyAmount;
            accountList.get(id).setMoneyAmount(money);
            System.out.println("Deposited " + moneyAmount + " to account " + id + ". New balance: " + money + "\n");
        }
        else
        {
            System.out.println("Error\n");
        }
    }

    public void accountWithdraw(Long id, int moneyAmount)
    {
        if ((accountList.values().stream().allMatch(i -> i.getId().equals(id))) && moneyAmount > 0)
        {
            int money = accountList.get(id).getMoneyAmount() - moneyAmount;
            if (money < 0)
            {
                System.out.println("Error: insufficient funds on account id=" +
                        id + " moneyAmount=" +
                        accountList.get(id).getMoneyAmount() +
                        ", attempted withdraw=" + moneyAmount + "\n");
            }
            else
            {
                accountList.get(id).setMoneyAmount(money);
                System.out.println("Withdraw " + moneyAmount + " to account " + id + ". New balance: " + money + "\n");
            }
        }
        else
        {
            System.out.println("Error\n");
        }
    }

    public void accountTransfer(Long initialId, Long finalId, int moneyAmount)
    {
        if ((accountList.values().stream()
                .allMatch(i -> i.getId().equals(initialId)
                        && i.getId().equals(finalId))) && moneyAmount > 0)
        {
            Account initialAcc = accountList.get(initialId);
            Account finalAcc = accountList.get(finalId);

            int initialMoneyAmount = initialAcc.getMoneyAmount() - moneyAmount;
            if (initialMoneyAmount < 0)
            {
                System.out.println("Error: insufficient funds on account id=" +
                        initialId + " moneyAmount=" +
                        initialAcc.getMoneyAmount() +
                        ", attempted transfer=" + moneyAmount + "\n");
            }
            else
            {
                initialAcc.setMoneyAmount(initialMoneyAmount);
                int finalMoneyAmount;
                if (!Objects.equals(initialAcc.getUserId(), finalAcc.getUserId()))
                {
                    int commis = (int) (moneyAmount - (moneyAmount * (1 - commission)));
                    finalMoneyAmount = finalAcc.getMoneyAmount() + moneyAmount - commis;
                    System.out.println("Transfer completed from account " + initialId +
                            " to account " + finalId +
                            ". Amount: " + moneyAmount + ", commission: " + commis +
                            ", recipient received: " + (moneyAmount - commis) + "\n");
                }
                else
                {
                    finalMoneyAmount = finalAcc.getMoneyAmount() + moneyAmount;
                    System.out.println("Transfer completed from account " + initialId +
                            " to account " + finalId +
                            ". Amount: " + moneyAmount + " (no commission, same user)\n");
                }
                finalAcc.setMoneyAmount(finalMoneyAmount);
            }
        }
        else
        {
            System.out.println("Error\n");
        }
    }

    public Long accountClose(Long id)
    {
        Long userId = 0L;
        if (accountList.values().stream().allMatch(i -> i.getId().equals(id)))
        {
            userId = accountList.get(id).getUserId();
            if ( accountList.values().stream()
                    .filter(i -> i.getUserId().equals(accountList.get(id).getUserId()))
                    .count() > 1)
            {
                int money = accountList.get(id).getMoneyAmount();
                Account acc = accountList.values().stream()
                        .filter(i -> i.getUserId().equals(accountList.get(id).getUserId()))
                        .filter(i -> !Objects.equals(i.getId(), id))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException("Not found"));
                acc.setMoneyAmount(acc.getMoneyAmount() + money);
                accountList.remove(id);
                System.out.println("Account " + id +
                        " closed. Remaining balance " + money +
                        " transferred to account " + acc.getId() + "\n");
            }
            else
            {
                System.out.println("Error\n");
                userId = 0L;
            }
        }
        else
        {
            System.out.println("Error\n");
        }
        return userId;
    }
}
