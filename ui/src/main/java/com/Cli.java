package com;

import Objects.dto.AccountDto;
import Objects.interfaces.AccountInterDto;
import Objects.interfaces.Safe;

import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class Cli {
    private final Safe safe;
    private final Scanner scanner;

    public Cli() {
        safe = new Vault();
        scanner = new Scanner(System.in);
    }

    public void run() {
        boolean exitCondition = false;

        while (!exitCondition) {
            exitCondition = mainMenu();
        }
    }

    private void printLogo() {
        System.out.println("logo");
    }

    private boolean mainMenu() {
        System.out.println("menu");
        System.out.println("1. register");
        System.out.println("2. login");
        System.out.println("3. exit");

        int action = getNumericInputFromUser(">> ");

        switch (action) {
            // can be changed to an enum instead of numeric
            case 1:
                register();
                break;

            case 2:
                return login();

            case 3:
                return true;

            default:
                System.out.println("There is no option " + action);
        }

        return false;
    }

    private String getTextInputFromUser(String textToPrint) {
        System.out.println(textToPrint);
        return scanner.nextLine();
    }

    private int getNumericInputFromUser(String textToPrint) {
        String userText = getTextInputFromUser(textToPrint);
        try {
            return Integer.parseInt(userText);
        } catch (NumberFormatException ignore) {
            System.out.println("there is no number with characters in it");
            return -1;
        }
    }

    private void register() {
        String username, password;

        System.out.println("Register:");
        username = getTextInputFromUser("Please enter a username: ");
        password = getTextInputFromUser("Please enter a password: ");

        try {
            safe.register(username, password);
        } catch (InvalidParameterException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private boolean login() {
        boolean userExit = false;
        String username, password, userSessionKey;

        System.out.println("Login:");
        username = getTextInputFromUser("Please enter a username: ");
        password = getTextInputFromUser("Please enter a password: ");

        try {
            userSessionKey = safe.login(username, password);
            while (!userExit) {
                userExit = userMenu(userSessionKey);
            }
        } catch (InvalidParameterException exception) {
            System.out.println(exception.getMessage());
        }

        return false;
    }

    private boolean userMenu(String userSessionKey) {
        System.out.println("\nuser menu:");
        System.out.println("1. change username");
        System.out.println("2. change password");
        System.out.println("3. add an accounts");
        System.out.println("4. show an accounts");
        System.out.println("5. show accounts list");
        System.out.println("6. delete an accounts");
        System.out.println("7. delete user");
        System.out.println("8. check password strength");
        System.out.println("9. exit");

        int action = getNumericInputFromUser(">> ");

        switch (action) {
            case 1:
                changeUsername(userSessionKey);
                break;

            case 2:
                changePassword(userSessionKey);
                break;

            case 3:
                safe.addAccountToUser(userSessionKey, createAccount());
                break;

            case 4:
                String account = getTextInputFromUser("What is the name of the account you want to view? ");
                System.out.println("\n" + safe.showAccount(userSessionKey, account) + "\n");
                break;

            case 5:
                AtomicInteger index = new AtomicInteger(1);
                System.out.println("Accounts: ");
                safe.showAccounts(userSessionKey).forEach(accountDto -> System.out.println(index.getAndIncrement()  + ". " + accountDto.getAccountName())); // System.out::println
                System.out.println("\n");
                break;

            case 6:
                String accountToDelete = getTextInputFromUser("What is the name of the account you want to delete? ");
                safe.removeAccount(userSessionKey, accountToDelete);
                break;

            case 7:
                if (getTextInputFromUser("Are you sure you want to delete this user ? <Y/N> ").equalsIgnoreCase("y"))
                    safe.deleteUser(userSessionKey);
                break;

            case 8:
                String password = getTextInputFromUser("Please enter a password to check ");
                System.out.println(safe.checkUserPassword(password));
                break;

            case 9:
                safe.logout(userSessionKey);
                return true;

            default:
                System.out.println("There is no option " + action);
        }

        return false;
    }

    private void changeUsername(String userSessionKey) {
        String oldUsername = getTextInputFromUser("Please enter your old username: ");
        String newUsername = getTextInputFromUser("Please enter your new username: ");

        safe.changeUserUsername(userSessionKey, oldUsername, newUsername);
    }

    private void changePassword(String userSessionKey) {
        String oldPassword = getTextInputFromUser("Please enter your old password: ");
        String newPassword = getTextInputFromUser("Please enter your new password: ");

        safe.changeUserPassword(userSessionKey, oldPassword, newPassword);
    }

    private AccountInterDto createAccount() {
        String password, notes = "";
        String accountName = getTextInputFromUser("Please enter the name of the account: ");
        String username = getTextInputFromUser("Please enter the username of the account: ");

        if (getTextInputFromUser("Do you want to generate a password? <Y/N>").equalsIgnoreCase("y"))
            password = safe.generatePassword();
        else
            password = getTextInputFromUser("Please enter the password of the account: ");

        String link = getTextInputFromUser("Please enter the link of the site: ");

        if (getTextInputFromUser("Do you want to enter any other notes about this account").equalsIgnoreCase("y"))
            notes = getTextInputFromUser("Please enter any notes you have about this account: ");

        return new AccountDto(accountName, username, password, link, notes);
    }
}
