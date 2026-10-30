package spirin.dev.model;

import java.util.List;

public class User {
    private final Long id;
    private String login;
    private final List<Account> accountList;

    public User(Long id, String login, List<Account> accountList) {
        this.id = id;
        this.login = login;
        this.accountList = accountList;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    @Override
    public String toString() {
        return "id: " + id + "; " +
                "login: " + login + "; " +
                "accounts: " + accountList;
    }
}
