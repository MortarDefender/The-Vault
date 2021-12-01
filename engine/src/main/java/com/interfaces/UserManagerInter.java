package com.interfaces;

import java.util.List;

import Objects.interfaces.AccountCategory;
import Objects.interfaces.AccountInterDto;


public interface UserManagerInter {

    void removeAccount(int userId, String accountName);
    void addNewAccount(int userId, AccountInterDto account);

    List<AccountInterDto> getAllUserAccounts(int userId);
    AccountInterDto getAccount(int userId, String accountName);
    List<AccountInterDto> getAccountsCategory(int userId, AccountCategory chosenCategory);

    void changeUserAccount(int userId, AccountInterDto account);
    void changeAccountLink(int userId, String accountName, String newLink);          // oldLink
    void changeAccountNotes(int userId, String accountName, String newNotes);        // oldNotes
    void changeAccountUsername(int userId, String accountName, String newUsername);  // oldUsername
    void changeAccountPassword(int userId, String accountName, String newPassword);  // oldPassword

    List<AccountInterDto> searchInAccounts(int userId, String searchText);
    List<AccountInterDto> sortAccounts(int userId, boolean ascendingOrder);
}
