package com.csci2020.backend;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Authentication {
    public static final String HMAC_AUTHENTICATION_ALGORITHM = "HmacSHA256";
    public static final String KEY_GENERATION_ALGORITHM = "PBKDF2WithHmacSHA256";

    /**
     * Randomly generates a 16-byte salt
     * @return A 16-byte salt
     */
    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password and salt to generate a secret key
     * @param password Password
     * @param salt Salt
     */
    public static byte[] hashPassword(String password, byte[] salt){
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 32767, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GENERATION_ALGORITHM);
            return factory.generateSecret(keySpec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Generate 32 random bytes to use as a challenge for a challenge-response
     * @see Authentication#HMAC(byte[], byte[])
     * @return 32 random bytes
     */
    public static byte[] generateChallenge(){
        SecureRandom random = new SecureRandom();
        byte[] challenge = new byte[32];
        random.nextBytes(challenge);
        return challenge;
    }

    /**
     * Completes a challenge-response using a key (user's password hash) and a challenge
     * @param key Key/Password Hash
     * @param challenge Challenge
     * @return Completed challenge-response byte array
     */
    public static byte[] HMAC(byte[] key, byte[] challenge){
        try {
            Mac mac = Mac.getInstance(HMAC_AUTHENTICATION_ALGORITHM);
            mac.init(new SecretKeySpec(key, HMAC_AUTHENTICATION_ALGORITHM));
            return mac.doFinal(challenge);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compares two challenge-response values to determine if they match. Returns true if the HMACS match, false otherwise
     * @param userHmac The user's HMAC based on their input password
     * @param serverHmac The server's HMAC based on the stored password hash
     * @return True
     */
    public static boolean challengeResult(byte[] userHmac, byte[] serverHmac){
        return MessageDigest.isEqual(userHmac, serverHmac);
    }
}
