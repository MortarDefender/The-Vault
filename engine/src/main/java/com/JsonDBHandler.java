package com;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.interfaces.DBHandler;
import com.interfaces.UserInter;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


// public enum JsonDBHandler implements DBHandler {
public class JsonDBHandler implements DBHandler {
    // Instance;

    private final Gson convertor;
    private final AtomicInteger usersId;
    private final Map<Integer, User> usersMap;
    private static JsonDBHandler instance = null;
    private static final Object handlerLock = new Object();
    private final String DB_NAME = "Engine/src/com/db/usersDB.json";
    // private final String DB_NAME = "/com/db/usersDB.json";
    // private final String DB_NAME = "db/usersDB.json";

    private JsonDBHandler() {
        this.convertor = new Gson();
        this.usersMap = getUsersMap();
        this.usersId = getUsersId();
    }

    public static JsonDBHandler getInstance() {
        if (instance == null) {
            synchronized (handlerLock) {
                if (instance == null)
                    instance = new JsonDBHandler();
            }
        }

        return instance;
    }

    @Override
    protected void finalize() {  // does not work
        writeToDB();
    }

    private Map<Integer, User> getUsersMap() {
        String fileData = readFromDB();

        if (fileData.equals(""))
            return new HashMap<>();

        Type usersListType = new TypeToken<Map<Integer, User>>() {}.getType();
        return convertor.fromJson(fileData, usersListType);
    }

    private AtomicInteger getUsersId() {
        AtomicInteger latestId = new AtomicInteger(-1);

        if (usersMap != null && !usersMap.isEmpty())
            usersMap.keySet().forEach(currentId -> latestId.set(Math.max(latestId.get(), currentId)));

        return latestId;
    }

    private String readFromDB() {
        String fileData = "";

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DB_NAME))) { // getClass().getResourceAsStream(DB_NAME))
            fileData = (String) in.readObject();
            // fileData = Crypto.decrypt(fileData, "password");  // can be decrypted
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return fileData;
    }

    private void writeToDB() {
        String fileData = convertor.toJson(usersMap);
        // fileData = Crypto.encrypt(fileData, "password");  // can encrypt the file

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DB_NAME))) { // getClass().getResource(DB_NAME).toExternalForm())
            out.writeObject(fileData);
            out.flush();
        } catch (IOException ignore) { }
    }

    private void isUserExits(int userId) {
        if (!usersMap.containsKey(userId)) {
            throw new InvalidParameterException("there is no user with this id");
        }
    }

    @Override
    public void changeUserUsername(int userId, String oldUsername, String newUsername) {
        isUserExits(userId);

        if (!usersMap.get(userId).getUsername().equals(Crypto.getHash(oldUsername))) {
            throw new InvalidParameterException("The username " + oldUsername + " is not the username of this account");
        }
        else {
            usersMap.get(userId).setUsername(Crypto.getHash(newUsername));
        }

        writeToDB();
    }

    @Override
    public void changeUserPassword(int userId, String oldPassword, String newPassword) {
        if (usersMap.containsKey(userId) && usersMap.get(userId).getPassword().equals(Crypto.getHash(oldPassword))) {
            usersMap.get(userId).setPassword(Crypto.getHash(newPassword));
        }
        else {
            throw new InvalidParameterException("The password " + oldPassword + " is not the password of this account");
        }

        writeToDB();
    }

    @Override
    public void removeUser(int userId) {
        isUserExits(userId);
        usersMap.remove(userId);

        writeToDB();
    }

    @Override
    public String getUsername(int userId) {
        isUserExits(userId);
        return usersMap.get(userId).getUsername();
    }
    
    @Override
    public UserInter getUser(int userId) {
        isUserExits(userId);
        return usersMap.get(userId);
    }

    @Override
    public boolean checkIfUserExits(int userId) {
        return usersMap.containsKey(userId);
    }

    @Override
    public boolean checkIfUserExits(String username) {
        String hashedUsername = Crypto.getHash(username);
        AtomicBoolean result = new AtomicBoolean(false);

        usersMap.values().forEach(user -> {
            if (user.getUsername().equals(hashedUsername))
                result.set(true);
        });

        return result.get();
    }

    @Override
    public boolean checkIfUserExits(String username, String password) {
        String hashedUsername = Crypto.getHash(username);
        String hashedPassword = Crypto.getHash(password);
        AtomicBoolean result = new AtomicBoolean(false);

        usersMap.values().forEach(user -> {
            if (user.getUsername().equals(hashedUsername) &&
                user.getPassword().equals(hashedPassword))
                result.set(true);
        });

        return result.get();
    }

    @Override
    public void addUser(String username, String password) {
        String hashedUsername = Crypto.getHash(username);
        String hashedPassword = Crypto.getHash(password);

        if (!checkIfUserExits(username, password)) {
            usersMap.put(usersId.incrementAndGet(), new User(hashedUsername, hashedPassword));
        }
        else {
            throw new InvalidParameterException("there is a user with this username and password");
        }

        writeToDB();
    }

    @Override
    public int getUserId(String username, String password) {
        String hashedUsername = Crypto.getHash(username);
        String hashedPassword = Crypto.getHash(password);
        AtomicInteger userId = new AtomicInteger(-1);

        usersMap.forEach((currentUserId, currentUser) -> {
            if (currentUser.getUsername().equals(hashedUsername) &&
                currentUser.getPassword().equals(hashedPassword))
                userId.set(currentUserId);
        });

        if (userId.get() > -1)
            return userId.get();

        throw new InvalidParameterException("There is no user with the username and password given");
    }

}
