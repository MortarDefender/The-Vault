package com.interfaces;

import Objects.dto.AccountDto;
import Objects.interfaces.AccountCategory;
import Objects.interfaces.AccountInterDto;


public interface AccountInter {

    String getDate();
    String getLink();
    String getNotes();
    AccountDto getDto();
    String getUsername();
    String getPassword();
    String getAccountName();
    AccountCategory getCategory();
    AccountInterDto getInterDto();

    void setLink(String link);
    void setNotes(String notes);
    void setUsername(String username);
    void setPassword(String password);
    void setAccountName(String name);
    void setAccount(AccountInterDto account);
    void setCategory(AccountCategory category);

    boolean search(String searchText);
    boolean searchCategory(AccountCategory category);
}
