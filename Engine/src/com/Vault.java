package com;

import Objects.interfaces.Safe;
import com.FileLogger.FileLogger;
import com.interfaces.DBHandler;
import Objects.dto.PasswordResults;
import com.interfaces.UserManagerInter;
import Objects.interfaces.AccountInterDto;
import Objects.interfaces.AccountCategory;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.security.InvalidParameterException;


public class Vault implements Safe {
    private final Crypto crypto = new Crypto();
    private final Logger logger = new FileLogger();
    private final DBHandler db = JsonDBHandler.getInstance(); // .Instance;
    private final Map<String, Integer> session = new HashMap<>();
    private final UserManagerInter userManager = new UserManager();

    public Vault() { }

    private int checkIfUserPresent(String userSessionKey) {
        if (!session.containsKey(userSessionKey)) {
            logger.warningActivity("There is no user with this session key: " + userSessionKey);
            throw new InvalidParameterException("There is no user with this session key");
        }
        return session.get(userSessionKey);
    }

    @Override
    public void deleteUser(String userSessionKey) {
        int userId = checkIfUserPresent(userSessionKey);
        db.removeUser(userId);
        logger.infoActivity("user with the session key: " + userSessionKey + ", has been deleted");
    }

    @Override
    public void changeUserUsername(String userSessionKey, String oldUsername, String newUsername) {
        int userId = checkIfUserPresent(userSessionKey);
        db.changeUserUsername(userId, oldUsername, newUsername);
        logger.infoActivity("The user with the username: " + oldUsername + " has change his username to " + newUsername);
    }

    @Override
    public void changeUserPassword(String userSessionKey, String oldPassword, String newPassword) {
        int userId = checkIfUserPresent(userSessionKey);
        db.changeUserPassword(userId, oldPassword, newPassword);
        logger.infoActivity("The user with the username: " + db.getUsername(userId) +
                " has change his password from " + oldPassword + " to " + newPassword);
    }

    /** check the given password for its strength */
    @Override
    public PasswordResults checkUserPassword(String password) {
        return Crypto.checkPasswordStrength(password);
    }

    @Override
    public String generatePassword() {
        return crypto.generatePassword();
    }

    @Override
    public String generatePassword(int passwordLength) {
        return crypto.generatePassword(passwordLength);
    }

    @Override
    public String generatePassword(int passwordLength, boolean useUpperLetters, boolean useLowerLetters,
                                   boolean useNumbers, boolean useSpecialCharacters) {
        return crypto.generatePassword(passwordLength, useUpperLetters, useLowerLetters, useNumbers, useSpecialCharacters);
    }

    @Override
    public List<AccountInterDto> showAccounts(String userSessionKey) {
        int userId = checkIfUserPresent(userSessionKey);
        return userManager.getAllUserAccounts(userId);
    }

    @Override
    public AccountInterDto showAccount(String userSessionKey, String accountName) {
        int userId = checkIfUserPresent(userSessionKey);
        return userManager.getAccount(userId, accountName);
    }

    @Override
    public List<AccountInterDto> showAccountsOfCategory(String userSessionKey, AccountCategory chosenCategory) {
        int userId = checkIfUserPresent(userSessionKey);
        return userManager.getAccountsCategory(userId, chosenCategory);
    }

    @Override
    public void removeAccount(String userSessionKey, String accountName) {
        int userId = checkIfUserPresent(userSessionKey);
        userManager.removeAccount(userId, accountName);
        logger.infoActivity("The user with the username: " +  db.getUsername(userId) + " has removed the account with the name " + accountName);
    }

    @Override
    public void addAccountToUser(String userSessionKey, AccountInterDto account) {
        int userId = checkIfUserPresent(userSessionKey);
        userManager.addNewAccount(userId, account);
        logger.infoActivity("The user with the username: " +  db.getUsername(userId) + " has added an account with the name " + account.getAccountName());
    }

    @Override
    public void changeUserAccountLink(String userSessionKey, String accountName, String newLink) {
        int userId = checkIfUserPresent(userSessionKey);
        userManager.changeAccountLink(userId, accountName, newLink);
        logger.infoActivity("The user with the username: " +  db.getUsername(userId) +
                " has changed the link of the account with the name " + accountName + " to the link " + newLink);
    }

    @Override
    public void changeUserAccountNotes(String userSessionKey, String accountName, String newNotes) {
        int userId = checkIfUserPresent(userSessionKey);
        userManager.changeAccountNotes(userId, accountName, newNotes);
        logger.infoActivity("The user with the username: " +  db.getUsername(userId) +
                " has changed the notes of the account with the name " + accountName + " to the notes " + newNotes);
    }

    @Override
    public void changeUserAccountUsername(String userSessionKey, String accountName, String newUsername) {
        int userId = checkIfUserPresent(userSessionKey);
        userManager.changeAccountUsername(userId, accountName, newUsername);
        logger.infoActivity("The user with the username: " +  db.getUsername(userId) +
                " has changed the username of the account with the name " + accountName + " to the username " + newUsername);
    }

    @Override
    public void changeUserAccountUPassword(String userSessionKey, String accountName, String newPassword) {
        int userId = checkIfUserPresent(userSessionKey);
        userManager.changeAccountPassword(userId, accountName, newPassword);
        logger.infoActivity("The user with the username: " +  db.getUsername(userId) +
                " has changed the password of the account with the name " + accountName + " to the password " + newPassword);
    }

    @Override
    public void changeUserAccount(String userSessionKey, AccountInterDto account) {
        int userId = checkIfUserPresent(userSessionKey);
        userManager.changeUserAccount(userId, account);
        logger.infoActivity("The user with the username: " +  db.getUsername(userId) + " has changed the account with the name " + account.getAccountName());
    }

    @Override
    public List<AccountInterDto> searchInAccounts(String userSessionKey, String searchText) {
        int userId = checkIfUserPresent(userSessionKey);
        return userManager.searchInAccounts(userId, searchText);
    }

    @Override
    public List<AccountInterDto> sortAccounts(String userSessionKey, boolean ascendingOrder) {
        int userId = checkIfUserPresent(userSessionKey);
        return userManager.sortAccounts(userId, ascendingOrder);
    }

    @Override
    public void register(String username, String password) {
        if (db.checkIfUserExits(username, password)) {
            logger.warningActivity("There is no user with this username: " + username);
            throw new InvalidParameterException("There is a user with the username '" + username + "'");
        }
        else if (username.equals("")) {
            throw new InvalidParameterException("Username parameter cant be empty");
        } else if (password.equals("")) {
            throw new InvalidParameterException("Password parameter cant be empty");
        } else {
            db.addUser(username, password);
            logger.infoActivity("The user with the username " + username  + " has been registered");
        }
    }

    @Override
    public String login(String username, String password) {
        if (db.checkIfUserExits(username, password)) {
            String sessionKey = Crypto.getSessionKey();

            while (session.containsKey(sessionKey)) {
                sessionKey = Crypto.getSessionKey();
            }

            session.put(sessionKey, db.getUserId(username, password));
            logger.infoActivity("The user with the username " + username + " has been logged in");
            return sessionKey;
        }
        else {
            logger.warningActivity("User has been trying to enter with the username " + username + " and password " + password);
            throw new InvalidParameterException("There is no user with the username '" + username + "'");
        }
    }

    @Override
    public void logout(String userSessionKey) {
        int userId = checkIfUserPresent(userSessionKey);
        session.remove(userSessionKey);
        logger.warningActivity("The user with the session key: " + userSessionKey + " and username " + db.getUsername(userId) + " has logged out");
    }

}
