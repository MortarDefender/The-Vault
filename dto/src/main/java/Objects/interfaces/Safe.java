package Objects.interfaces;

import Objects.dto.PasswordResults;

import java.util.List;


public interface Safe {
    void deleteUser(String userSessionKey);
    void changeUserPassword(String userSessionKey, String oldPassword, String newPassword);
    void changeUserUsername(String userSessionKey, String oldUsername, String newUsername);

    String generatePassword();
    String generatePassword(int passwordLength);
    String generatePassword(int passwordLength, boolean useUpperLetters, boolean useLowerLetters, boolean useNumbers, boolean useSpecialCharacters);

    void logout(String userSessionKey);
    String login(String username, String password);
    void register(String username, String password);

    PasswordResults checkUserPassword(String password);  // change name

    void removeAccount(String userSessionKey, String accountName);
    void addAccountToUser(String userSessionKey, AccountInterDto account);
    void changeUserAccount(String userSessionKey, AccountInterDto account);
    void changeUserAccountLink(String userSessionKey, String accountName, String newLink);
    void changeUserAccountNotes(String userSessionKey, String accountName, String newNotes);
    void changeUserAccountUsername(String userSessionKey, String accountName, String newUsername);
    void changeUserAccountUPassword(String userSessionKey, String accountName, String newPassword);

    List<AccountInterDto> showAccounts(String userSessionKey);
    AccountInterDto showAccount(String userSessionKey, String accountName);
    List<AccountInterDto> showAccountsOfCategory(String userSessionKey, AccountCategory chosenCategory);

    List<AccountInterDto> searchInAccounts(String userSessionKey, String searchText);
    List<AccountInterDto> sortAccounts(String userSessionKey, boolean ascendingOrder);
}
