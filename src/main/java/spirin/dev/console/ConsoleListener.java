package spirin.dev.console;

import org.springframework.stereotype.Component;
import spirin.dev.service.AccountService;
import spirin.dev.service.UserService;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class ConsoleListener {
    Scanner scanner = new Scanner(System.in);

    UserService userService;
    AccountService accountService;

    public ConsoleListener(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    private void usercreate() {
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        if (!login.isBlank()) {
            try {
                String newUser = userService.addUser(login);
                System.out.println("User created: " + newUser + "\n");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + "\n");
            }
        } else {
            System.out.println("Error: The login cannot be empty.\n");
        }
    }

    private void showAllUsers() {
        userService.showAllUsers();
        System.out.println();
    }

    private void accountCreate() {
        System.out.print("Enter user id: ");
        Long userID = scanner.nextLong();
        scanner.nextLine();
        try {
            String newAcc = userService.createAccountForUser(userID);
            System.out.println("Account created: " + newAcc + "\n");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private void accountDeposit() {
        System.out.print("Enter account id: ");
        Long accountID = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter amount: ");
        int moneyAmount = scanner.nextInt();
        scanner.nextLine();
        try {
            int money = accountService.accountDeposit(accountID, moneyAmount);
            System.out.println("Deposited " + moneyAmount +
                    " to account " + accountID +
                    ". New balance: " +
                    money + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private void accountWithdraw() {
        System.out.print("Enter account id: ");
        Long accountID = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter amount: ");
        int moneyAmount = scanner.nextInt();
        scanner.nextLine();
        try {
            int money = accountService.accountWithdraw(accountID, moneyAmount);
            System.out.println("Withdraw " + moneyAmount +
                    " to account " + accountID +
                    ". New balance: " +
                    money + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private void accountTransfer() {
        System.out.print("Enter source account id: ");
        Long initialAccountID = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter target account id: ");
        Long finalAccountID = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter amount: ");
        int moneyAmount = scanner.nextInt();
        scanner.nextLine();
        try {
            int commis = accountService.accountTransfer(initialAccountID, finalAccountID, moneyAmount);
            System.out.println("Transfer completed from account " + initialAccountID +
                    " to account " + finalAccountID +
                    ". Amount: " + moneyAmount + ", commission: " + commis +
                    ", recipient received: " + (moneyAmount - commis) + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private void accountClose() {
        System.out.print("Enter account id to close: ");
        Long accountID = scanner.nextLong();
        scanner.nextLine();
        try {
            System.out.println(userService.accountCloseForUser(accountID));
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    public void start() {
        System.out.println("MiniBank started. Type EXIT to stop.\n" +
                "Available commands: USER_CREATE, SHOW_ALL_USERS, ACCOUNT_CREATE,\n" +
                "ACCOUNT_DEPOSIT, ACCOUNT_WITHDRAW, ACCOUNT_TRANSFER, ACCOUNT_CLOSE, EXIT\n");

        while (true) {
            System.out.print("Enter command: ");
            String option = scanner.nextLine();

            switch (option) {
                case "USER_CREATE" -> usercreate();
                case "SHOW_ALL_USERS" -> showAllUsers();
                case "ACCOUNT_CREATE" -> accountCreate();
                case "ACCOUNT_DEPOSIT" -> accountDeposit();
                case "ACCOUNT_WITHDRAW" -> accountWithdraw();
                case "ACCOUNT_TRANSFER" -> accountTransfer();
                case "ACCOUNT_CLOSE" -> accountClose();
                case "EXIT" -> {
                    System.out.println("MiniBank stopped.");
                    return;
                }
                default -> System.out.println("Error\n");
            }
        }
    }
}
