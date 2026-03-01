package com.csci2020.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Class representing an account used to log in
 */
@Entity
@Table(name = "accounts")
public class Account {
    /**
     * The player's unique username
     */
    String username;

    /**
     * The player associated with this account
     */
    Player player;

    /**
     * Password Salt
     */
    byte[] salt;

    /**
     * Hashed password
     */
    byte[] passwordHash;
}