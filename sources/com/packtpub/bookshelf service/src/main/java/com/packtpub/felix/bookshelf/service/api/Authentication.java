package com.packtpub.felix.bookshelf.service.api;

import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;

public interface Authentication {
    String login(String username, char[] password) throws InvalidCredentialException;
    void logout(String sessionId);
    boolean sessionIsValid(String sessionId);
}
