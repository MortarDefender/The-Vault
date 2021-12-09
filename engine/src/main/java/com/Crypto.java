package com;

import com.interfaces.UserInter;
import Objects.dto.PasswordResults;
import Objects.dto.PasswordStrength;

import java.util.*;
import java.util.Random;
import java.util.stream.IntStream;

public class Crypto {
    private final Random random = new Random();
    private static final EncryptionScheme encryptionScheme = new AES();

    public static String encrypt(String plainText, String password) {
        return encryptionScheme.encrypt(plainText, password);
    }

    public static String decrypt(String cypherText, String password) {
        return encryptionScheme.decrypt(cypherText, password);
    }

    public static String getHash(String text) {
        return HashFunction.SHA1(text);
    }

    public String generatePassword() {
        int lowerBoundLength = 8, upperBoundLength = 20, preFixedLength = 8;
        return generatePassword(preFixedLength + random.nextInt(upperBoundLength - lowerBoundLength));
    }

    public String generatePassword(int passwordLength) {
        return generatePassword(passwordLength, new ArrayList<>(Collections.singletonList("English")));
    }

    public String generatePassword(int passwordLength, boolean useUpperLetters, boolean useLowerLetters,
                                   boolean useNumbers, boolean useSpecialCharacters) {
        if (!useNumbers && !useLowerLetters && !useUpperLetters && !useSpecialCharacters)
            return "";

        StringBuilder password = new StringBuilder();
        List<String> dictionary = new ArrayList<>();
        if (useNumbers)
            dictionary.add("0123456789");
        if (useSpecialCharacters)
            dictionary.add("!@#$%^&*()_+-=[]{}?~");
        if (useUpperLetters)
            dictionary.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        if (useLowerLetters)
            dictionary.add("abcdefghijklmnopqrstuvwxyz");

        IntStream.range(0, passwordLength).forEach(
                i -> {
                    String chosenPart = dictionary.get(random.nextInt(dictionary.size()));
                    password.append(chosenPart.charAt(random.nextInt(chosenPart.length())));
                }
        );

        return password.toString();
    }

    private String generatePassword(int passwordLength, List<String> possibleLanguages) { // other languages can be added
        // upper case, numbers, special letters, password length above 8
        return generatePassword(passwordLength, true, true, true, true);
    }

    public static PasswordResults checkPasswordStrength(String userPassword) {
        int score = 0;
        List<String> listOfConcerns = new ArrayList<>();
        String numbers = "0123456789";
        String specialLetters = "!@#$%^&*()_+-=[]{}?~";
        String upperLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerLetters = "abcdefghijklmnopqrstuvwxyz";

        if (userPassword.length() >= 8) {
            score += 5;
        }
        else {
            listOfConcerns.add("A good password needs to be at least 8 letters or higher");
            return new PasswordResults(PasswordStrength.Bad, listOfConcerns);
        }

        if (userPassword.contains(specialLetters)) {
            score += 2;
        }
        else {
            listOfConcerns.add("A good password needs a few special letters");
        }

        if (userPassword.contains(numbers)) {
            score += 3;
        }
        else {
            listOfConcerns.add("A good password needs a few numbers");
        }

        if (userPassword.contains(lowerLetters)) {
            score += 3;
        }
        else {
            listOfConcerns.add("A good password needs a combination of lower and upper case letters");
        }

        if (userPassword.contains(upperLetters)) {
            score += 4;
        }
        else {
            listOfConcerns.add("A good password needs a few upper case letters");
        }

        if (score >= 14)
            return new PasswordResults(PasswordStrength.VeryGood, listOfConcerns);
        else if (score > 10)
            return new PasswordResults(PasswordStrength.Good, listOfConcerns);
        return new PasswordResults(PasswordStrength.Bad, listOfConcerns);
    }

    private String addSpecialLetters(String password) {
        Map<String, String> specialLettersMap = new HashMap<>();
        specialLettersMap.put("a", "@");
        specialLettersMap.put("s", "$");
        specialLettersMap.put("S", "$");
        specialLettersMap.put("l", "!");
        specialLettersMap.put("t", "+");

        final String[] newPassword = {""};
        password.chars().mapToObj(letter -> "" + letter).forEach(letter -> {
                if (specialLettersMap.containsKey(letter)) {
                    if (random.nextInt(2) == 1)
                        letter = specialLettersMap.get(letter);

                }

            newPassword[0] = newPassword[0].concat(letter);
            }
        );

        return newPassword[0];
    }

    public static String getSessionKey() {
        final int keyLength = 64;
        Random random = new Random();
        StringBuilder sessionKey = new StringBuilder();
        String dictionary = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        IntStream.range(0, keyLength).forEach(i -> sessionKey.append(dictionary.charAt(random.nextInt(dictionary.length()))));

        return sessionKey.toString();
    }

    public static String getUserPassword(UserInter user) {
        return Crypto.getHash(user.getUsername() + "" + user.getPassword());
    }
}
