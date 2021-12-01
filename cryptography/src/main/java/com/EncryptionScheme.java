package com;

public interface EncryptionScheme {
    String encrypt(String plainText, String key);
    String decrypt(String cypherText, String key);
}
