package spirin.dev.console;

import org.springframework.stereotype.Controller;
import spirin.dev.service.AccountService;
import spirin.dev.service.UserService;

import java.util.Scanner;

@Controller
public class ConsoleListener {
    Scanner scanner = new Scanner(System.in);

    UserService userService;
    AccountService accountService;

    public ConsoleListener(UserService userService, AccountService accountService)
    {
        this.userService = userService;
        this.accountService = accountService;
    }

    public void Start()
    {
        System.out.println("MiniBank started. Type EXIT to stop.\n" +
                "Available commands: USER_CREATE, SHOW_ALL_USERS, ACCOUNT_CREATE,\n" +
                "ACCOUNT_DEPOSIT, ACCOUNT_WITHDRAW, ACCOUNT_TRANSFER, ACCOUNT_CLOSE, EXIT\n");
        while (true)
        {
            System.out.print("Enter command: ");
            String option = scanner.nextLine();

            if (option.equals("USER_CREATE"))
            {
                System.out.print("Enter login: ");
                String login = scanner.nextLine();
                if (!login.isBlank())
                {
                    userService.addUser(login);
                }
                else
                {
                    System.out.println("Error\n");
                }
            }
            else if (option.equals("SHOW_ALL_USERS"))
            {
                userService.showAllUsers();
                System.out.println();
            }
            else if (option.equals("ACCOUNT_CREATE"))
            {
                System.out.print("Enter user id: ");
                Long userID = scanner.nextLong();
                scanner.nextLine();
                userService.createAccountForUser(userID);
            }
            else if (option.equals("ACCOUNT_DEPOSIT"))
            {
                System.out.print("Enter account id: ");
                Long accountID = scanner.nextLong();
                scanner.nextLine();
                System.out.print("Enter amount: ");
                int moneyAmount = scanner.nextInt();
                scanner.nextLine();
                accountService.accountDeposit(accountID, moneyAmount);
            }
            else if (option.equals("ACCOUNT_WITHDRAW"))
            {
                System.out.print("Enter account id: ");
                Long accountID = scanner.nextLong();
                scanner.nextLine();
                System.out.print("Enter amount: ");
                int moneyAmount = scanner.nextInt();
                scanner.nextLine();
                accountService.accountWithdraw(accountID, moneyAmount);
            }
            else if (option.equals("ACCOUNT_TRANSFER"))
            {
                System.out.print("Enter source account id: ");
                Long initialAccountID = scanner.nextLong();
                scanner.nextLine();
                System.out.print("Enter target account id: ");
                Long finalAccountID = scanner.nextLong();
                scanner.nextLine();
                System.out.print("Enter amount: ");
                int moneyAmount = scanner.nextInt();
                scanner.nextLine();
                accountService.accountTransfer(initialAccountID, finalAccountID, moneyAmount);
            }
            else if (option.equals("ACCOUNT_CLOSE"))
            {
                System.out.print("Enter account id to close: ");
                Long accountID = scanner.nextLong();
                scanner.nextLine();
                userService.accountCloseForUser(accountID);
            }
            else if (option.equals("EXIT"))
            {
                System.out.println("MiniBank stopped.");
                break;
            }
            else
            {
                System.out.println("Error\n");
            }
        }
    }
}
