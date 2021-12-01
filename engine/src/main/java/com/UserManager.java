package com;

import com.google.gson.Gson;
import com.interfaces.DBHandler;
import com.interfaces.UserManagerInter;
import com.google.gson.reflect.TypeToken;
import Objects.interfaces.AccountInterDto;
import Objects.interfaces.AccountCategory;

import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;


public class UserManager implements UserManagerInter {

    @FunctionalInterface
    private interface AccountFunction {
        void Invoke(Map<String, Account> userAccounts, Account account);  // AccountInter
    }

    @FunctionalInterface
    private interface ChangeAccountFunction {
        void Invoke(Account account);
    }

    private final Gson convertor = new Gson();
    private final String UserFilePath = "Engine/src/com/usersFiles/";
    // private final String UserFilePath = "usersFiles/";
    private final DBHandler db = JsonDBHandler.getInstance(); // .Instance;

    @Override
    public void addNewAccount(int userId, AccountInterDto account) {
        accountHandler(userId, new Account(account), UserManager::addAccount);
    }

    private static void addAccount(Map<String, Account> userAccounts, Account account) {
        userAccounts.put(account.getAccountName(), account);
    }

    @Override
    public void removeAccount(int userId, String accountName) {
        accountHandler(userId, new Account(accountName), UserManager::deleteAccount);
    }

    private static void deleteAccount(Map<String, Account> userAccounts, Account account) {
        userAccounts.remove(account.getAccountName());
    }

    @Override
    public List<AccountInterDto> getAllUserAccounts(int userId) {
        List<AccountInterDto> userAccountsList = new ArrayList<>();
        accountHandler(userId, ((userAccounts, account) ->
            userAccounts.values().forEach(userAccount -> userAccountsList.add(userAccount.getInterDto()))
        ));
        return userAccountsList;
    }

    @Override
    public AccountInterDto getAccount(int userId, String accountName) {
        return getAllUserAccounts(userId).stream()
                .filter(accountInterDto -> accountInterDto.getAccountName().equals(accountName))
                .findFirst().orElseThrow(() -> new InvalidParameterException(""));
    }

    @Override
    public List<AccountInterDto> getAccountsCategory(int userId, AccountCategory chosenCategory) {
        List<AccountInterDto> resultsAccount = new ArrayList<>();

        accountHandler(userId,
            ((userAccounts, account) ->
                    userAccounts.values().forEach(
                            currentAccount -> {
                                if (currentAccount.searchCategory(chosenCategory))
                                    resultsAccount.add(currentAccount.getInterDto());
                            }
                    )
            )
        );

        return resultsAccount;
    }

    @Override
    public List<AccountInterDto> searchInAccounts(int userId, String searchText) {
        List<AccountInterDto> resultedAccounts = new ArrayList<>();

        accountHandler(userId,
                (userAccounts, account) ->
                    userAccounts.values().forEach(
                            currentAccount -> {
                                if (currentAccount.search(searchText))
                                    resultedAccounts.add(currentAccount.getInterDto());
                            }
                    )
                );

        return resultedAccounts;
    }

    @Override
    public List<AccountInterDto> sortAccounts(int userId, boolean ascendingOrder) {
        List<AccountInterDto> resultedAccounts = new ArrayList<>();

        accountHandler(userId,
                (userAccounts, account) ->
                    userAccounts.values().forEach(
                            currentAccount -> resultedAccounts.add(currentAccount.getInterDto())
                    )
                );

        if (ascendingOrder)
            resultedAccounts.sort(AccountInterDto::compareTo);
        else
            resultedAccounts.sort( (account1, account2) -> -account1.compareTo(account2));

        return resultedAccounts;
    }

    @Override
    public void changeAccountUsername(int userId, String accountName, String newUsername) {
        changeAccount(userId, accountName, account -> account.setUsername(newUsername));
    }

    @Override
    public void changeAccountPassword(int userId, String accountName, String newPassword) {
        changeAccount(userId, accountName, account -> account.setPassword(newPassword));
    }

    @Override
    public void changeAccountLink(int userId, String accountName, String newLink) {
        changeAccount(userId, accountName, account -> account.setLink(newLink));
    }

    @Override
    public void changeAccountNotes(int userId, String accountName, String newNotes) {
        changeAccount(userId, accountName, account -> account.setNotes(newNotes));
    }

    @Override
    public void changeUserAccount(int userId, AccountInterDto newAccount) {
        changeAccount(userId, newAccount.getAccountName(),
            account -> account.setAccount(newAccount)
        );
    }

    private void changeAccount(int userId, String accountName, ChangeAccountFunction changeFunction) {
        accountHandler(userId, (userAccounts, account) -> {
            userAccounts.values().forEach(
                    userAccount -> {
                        if (userAccount.getAccountName().equals(accountName)) {
                            changeFunction.Invoke(userAccount);
                        }
                    }
            );
        });
    }

    private void accountHandler(int userId, AccountFunction accountFunction) {
        accountHandler(userId, new Account(""), accountFunction);
    }

    private void accountHandler(int userId, Account account, AccountFunction accountFunction) {
        // can be changed from gson to a class / interface that converts the data from the user file into a Map
        if (db.checkIfUserExits(userId)) {
            String username = db.getUsername(userId);
            String plainText = Crypto.decrypt(readDataFromFile(username), Crypto.getUserPassword(db.getUser(userId)));
            Type usersAccountType = new TypeToken<Map<String, Account>>() {}.getType();
            Map<String, Account> userAccounts = convertor.fromJson(plainText, usersAccountType);
            if (userAccounts == null && plainText.equals(""))
                userAccounts = new HashMap<>();
            accountFunction. Invoke(userAccounts, account);
            String jsonAccounts = convertor.toJson(userAccounts);
            String cypherText = Crypto.encrypt(jsonAccounts, Crypto.getUserPassword(db.getUser(userId)));
            writeDataToFile(username, cypherText);
        }
        else {
            throw new InvalidParameterException("There is no user with this credentials "); // improve ?
        }
    }

    private String readDataFromFile(String username) {
        String fileName = getFileName(username), fileData = "";

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) { // getClass().getResourceAsStream(getFileName(username)))
            fileData = (String) in.readObject();
        } catch (IOException | ClassNotFoundException ignore) {}

        return fileData;
    }

    private void writeDataToFile(String username, String dataToWrite) {
        String fileName = getFileName(username);
        // String fileName = getClass().getResource(getFileName(username)).toExternalForm();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(dataToWrite);
            out.flush();
        } catch (IOException ignore) {}
    }

    private String getFileName(String username) {
        return UserFilePath + username + ".txt";  // file type can be change to a more "secure" / confusing format
    }

}
