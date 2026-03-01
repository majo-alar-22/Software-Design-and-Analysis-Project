package com.csci2020.backend;

public class Account {
    String username;
    Player player;
    byte[] salt;
    byte[] passwordHash;
}