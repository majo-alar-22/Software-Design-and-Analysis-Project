package com.csci2020.backend;

public record AuthenticationResult(AUTHENTICATION_STATUS status, String message) {
    public enum AUTHENTICATION_STATUS {
        SUCCESS,
        INVALID_FORMAT,
        INVALID_CREDENTIALS,
        INSUFFICIENT_PERMISSIONS,
        ERROR;
    }
}