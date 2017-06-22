package com.packtpub.felix.bookshelf.service.exceptions;

public class InvalidCredentialException extends Exception {
    public InvalidCredentialException(String username) {
        super(username);
    }
}
