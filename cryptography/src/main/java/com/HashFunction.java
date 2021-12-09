package com;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashFunction {
    private enum HashTypes {
        MD2, MD5, SHA1, SHA224,
        SHA256, SHA384, SHA512;

        public String getFunctionName() {
            switch (this) {
                case SHA1:
                    return "SHA-1";
                case SHA224:
                    return "SHA-224";
                case SHA256:
                    return "SHA-256";
                case SHA384:
                    return "SHA-384";
                case SHA512:
                    return "SHA-512";
                default:
                    return this.name();
            }
        }
    }

    public static String MD2(String password) { return getHashedPassword(password, HashTypes.MD2); }

    public static String MD5(String password) { return getHashedPassword(password, HashTypes.MD5); }

    public static String SHA1(String password) { return getHashedPassword(password, HashTypes.SHA1); }

    public static String SHA224(String password) { return getHashedPassword(password, HashTypes.SHA224); }

    public static String SHA256(String password) { return getHashedPassword(password, HashTypes.SHA256); }

    public static String SHA384(String password) { return getHashedPassword(password, HashTypes.SHA384); }

    public static String SHA512(String password) { return getHashedPassword(password, HashTypes.SHA512); }

    private static String getHashedPassword(String password, HashTypes hashFunction) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashFunction.getFunctionName());
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));

            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }

            return hashtext.toString();
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
