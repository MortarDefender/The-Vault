package com.interfaces;

public interface DBHandler {
    void removeUser(int userId);
    UserInter getUser(int userId);
    String getUsername(int userId);
    void addUser(String username, String password);
    int getUserId(String username, String password);
    void changeUserPassword(int userId, String oldPassword, String newPassword);
    void changeUserUsername(int userId, String oldUsername, String newUsername);

    boolean checkIfUserExits(int userId);
    boolean checkIfUserExits(String username);
    boolean checkIfUserExits(String username, String password);
}
