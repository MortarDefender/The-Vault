package Objects.dto;

import Objects.interfaces.AccountCategory;
import Objects.interfaces.AccountInterDto;


public class AccountDto implements AccountInterDto, Comparable<AccountInterDto> {
    private final AccountCategory category;
    private final String accountName, username, password, link, notes;

    public AccountDto(String accountName, String username, String password, String link, String notes) {
        this(accountName, username, password, link, notes, AccountCategory.Default);
    }

    public AccountDto(String accountName, String username, String password, String link, String notes, AccountCategory category) {
        this.link = link;
        this.notes = notes;
        this.category = category;
        this.username = username;
        this.password = password;
        this.accountName = accountName;
    }

    @Override
    public String getLink() { return link; }

    @Override
    public String getNotes() { return notes; }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getAccountName() { return accountName; }

    @Override
    public AccountCategory getCategory() { return category; }

    @Override
    public int compareTo(AccountInterDto o) {  // can be using fields array
        return getAccountName().compareTo(o.getAccountName());
    }

    @Override
    public String toString() {
        return String.format("Account Name: %s\nUsername: %s\nPassword: %s\nLink: %s\nNotes: %s", accountName, username, password, link, notes);
    }
}
