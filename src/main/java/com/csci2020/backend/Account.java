package com.csci2020.backend;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing an account used to log in
 */
@Entity
@Table(name = "accounts")
public class Account {
    /**
     * The player's unique username
     */
    @Id
    @Column(name = "username", unique = true)
    String username;

    /**
     * The player associated with this account
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    Player player;

    /**
     * Password Salt
     */
    @Column(name = "salt")
    byte[] salt;

    /**
     * Hashed password
     */
    @Column(name = "passwordhash")
    byte[] passwordHash;

    @Column(name = "administrator")
    private boolean admin = false;

    /**
     * Default constructor. This should not be used, but is required for Hibernate.
     */
    @Deprecated(forRemoval = false)
    public Account(){

    }

    /**
     * Create a new account with the specified username and player
     * @param username
     * @param player
     */
    public Account(String username, Player player, byte[] salt, byte[] passwordHash, boolean isAdmin){
        this.username = username;
        this.player = player;
        this.salt = salt;
        this.passwordHash = passwordHash;
        this.admin = isAdmin;
    }

//    private static Logger accountLogger = Logging.createLogger("account", Path.of(".","accounts.log"));


    /**
     * Gets the player that is associated with this account
     * @return
     */
    public Player getPlayer(){
        return this.player;
    }

    public byte[] getSalt(){
        return this.salt;
    }
    public byte[] getPasswordHash(){
        return this.passwordHash;
    }

    /**
     * Returns if this player is an administrator
     * @return
     */
    public boolean isAdmin() {
        return admin;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other)
            return true;
        if(other == null)
            return false;
        if(other instanceof Account)
            return this.username.equals(((Account) other).username);
        return false;
    }

//    For testing
//    public String toString(){
//        StringBuilder sb = new StringBuilder("(");
//        for(Field f : this.getClass().getDeclaredFields()){
//            f.setAccessible(true);
//            sb.append(f.getName());
//            sb.append(": ");
//            try {
//                sb.append(f.get(this));
//            } catch (IllegalAccessException e) {
//                sb.append("Error");
//                e.printStackTrace();
//            }
//        }
//        sb.append(")");
//        return sb.toString();
//    }
}