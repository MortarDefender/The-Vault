package com;

import Objects.dto.AccountDto;
import com.interfaces.AccountInter;
import Objects.interfaces.AccountCategory;
import Objects.interfaces.AccountInterDto;

import java.util.Date;
import java.util.Arrays;
import java.text.SimpleDateFormat;


public class Account implements AccountInter {
    private AccountCategory category;
    private String accountName, username, password, link, notes, date;


    public Account(String accountName) {
        this(accountName, "", "", "");
    }

    public Account(String accountName, String username, String password) {
        this(accountName, username, password, "");
    }

    public Account(String accountName, String username, String password, String link) {
        this(accountName, username, password, link, "", AccountCategory.Default);
    }

    public Account(String accountName, String username, String password, String link, String notes, AccountCategory category) {
        this.link = link;
        this.notes = notes;
        this.category = category;
        this.username = username;
        this.password = password;
        this.accountName = accountName;
        this.date = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
    }

    public Account(AccountInterDto accountInterDto) {
        this(accountInterDto.getAccountName(), accountInterDto.getUsername(), accountInterDto.getPassword(),
             accountInterDto.getLink(), accountInterDto.getNotes(), accountInterDto.getCategory());
    }

    @Override
    public String getDate() { return date; }

    @Override
    public String getLink() { return link; }

    @Override
    public String getNotes() { return notes; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getAccountName() { return accountName; }

    @Override
    public AccountCategory getCategory() { return category; }

    @Override
    public AccountDto getDto() { return new AccountDto(accountName, username, password, link, notes, category); }

    @Override
    public AccountInterDto getInterDto() { return new AccountDto(accountName, username, password, link, notes, category); }

    @Override
    public void setLink(String link) {
        setDate();
        this.link = link;
    }

    @Override
    public void setNotes(String notes) {
        setDate();
        this.notes = notes;
    }

    @Override
    public void setPassword(String password) {
        setDate();
        this.password = password;
    }

    @Override
    public void setUsername(String username) {
        setDate();
        this.username = username;
    }

    @Override
    public void setAccountName(String accountName) {
        setDate();
        this.accountName = accountName;
    }

    @Override
    public void setCategory(AccountCategory category) {
        setDate();
        this.category = category;
    }

    private void setDate() { this.date = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()); }

    @Override
    public void setAccount(AccountInterDto account) {
        setDate();
        this.link = account.getLink();
        this.notes = account.getNotes();
        this.category = account.getCategory();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.accountName = account.getAccountName();
    }

    @Override
    public boolean searchCategory(AccountCategory category) {
        return this.category.equals(category);  // contains
    }

    @Override
    public boolean search(String searchText) {
        return Arrays.stream(this.getClass().getDeclaredFields()).anyMatch(
                field -> {
                    try {
                        return ((String) field.get(this)).contains(searchText);
                    } catch (IllegalAccessException | ClassCastException ignore) {} // change to instanceof
                    return false;
                }
        );
    }
}
