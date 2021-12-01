package com;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;


public class AES implements EncryptionScheme {

    private static final int IV_LENGTH_BYTE = 12;
    private static final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
    private static final int SALT_LENGTH_BYTE = 16;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    @Override
    public String encrypt(String plainText, String key) {

        try {
            byte[] iv = getRandomNonce(IV_LENGTH_BYTE), salt = getRandomNonce(SALT_LENGTH_BYTE);

            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            SecretKey aesKeyFromPassword = getAESKeyFromPassword(key.toCharArray(), salt);

            cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                    .put(iv).put(salt).put(cipherText).array();

            return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
        }
        catch (Exception ignore)
        {   // System.out.println(e.getMessage());
            return plainText;
        }
    }

    @Override
    public String decrypt(String cypherText, String key) {

        try {
            byte[] decodedCypher = Base64.getDecoder().decode(cypherText.getBytes(UTF_8));
            ByteBuffer buffer = ByteBuffer.wrap(decodedCypher);

            byte[] iv = new byte[IV_LENGTH_BYTE], salt = new byte[SALT_LENGTH_BYTE];
            buffer.get(iv);
            buffer.get(salt);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            SecretKey aesKeyFromPassword = getAESKeyFromPassword(key.toCharArray(), salt);
            cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            byte[] plainText = cipher.doFinal(cipherText);

            return new String(plainText, UTF_8);
        }
        catch (Exception ignore)
        {   // System.out.println(e.getMessage());
            return cypherText;
        }
    }


    private byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }


    private SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterationCount = 65536, keyLength = 256;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");  // change key
        KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }
}
