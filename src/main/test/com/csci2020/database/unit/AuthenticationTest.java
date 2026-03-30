package com.csci2020.database.unit;

import com.csci2020.backend.Authentication;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {
    @Test
    public void testSalt(){
        byte[] firstSalt = Authentication.generateSalt();
        byte[] secondSalt = Authentication.generateSalt();
        byte[] emptyByte = new byte[firstSalt.length];
        assertFalse(Authentication.challengeResult(firstSalt, secondSalt));
        assertFalse(Authentication.challengeResult(firstSalt, emptyByte));
        assertTrue(Authentication.challengeResult(firstSalt, firstSalt));
    }

    @Test
    public void testHash(){
        byte[] emptyByte = new byte[16];
        char[] password = "password".toCharArray();
        byte[] hashed = Authentication.hashPassword(password, emptyByte);
        byte[] known = new byte[]{-46, 2, 70, 95, -96, 4, -43, 38, -55, -34, -22, 119, -116, 11, 107, -39, -124, 116, -5, 4, 16, 65, 11, 15, 89, 85, -105, -33, -127, -115, -112, 23};
        assertTrue(Authentication.challengeResult(known, hashed));
    }

    @Test
    public void testChallenge(){
        byte[] firstChallenge = Authentication.generateChallenge();
        byte[] secondChallenge = Authentication.generateChallenge();
        byte[] emptyByte = new byte[firstChallenge.length];
        assertFalse(Authentication.challengeResult(firstChallenge, secondChallenge));
        assertFalse(Authentication.challengeResult(firstChallenge, emptyByte));
        assertTrue(Authentication.challengeResult(firstChallenge, firstChallenge));
    }

    @Test
    public void testChallengeResponse(){
        byte[] hashed = Authentication.hashPassword("password".toCharArray(), new byte[16]);
        byte[] challenge = new byte[]{-117, 107, -47, 58, 115, -68, -49, -54, -108, 22, -100, -104, -91, -75, 116, -95, -91, -90, 101, -51, -126, 90, 4, 95, -105, -85, -56, 31, 54, 9, 83, -21};
        byte[] response = Authentication.HMAC(hashed, challenge);
        System.out.println(Arrays.toString(response));
        byte[] known = new byte[]{-22, -25, 52, 47, -80, 0, -87, -2, 88, 114, -106, 56, 83, -17, 120, -86, -57, 24, 17, -96, 6, -28, 76, 38, 43, 50, 36, -28, -25, 84, -54, 67};
        assertTrue(Authentication.challengeResult(response, known));
    }
}
