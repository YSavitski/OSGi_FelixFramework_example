package com.packtpub.felix.bookshelf.service.exceptions;

public class SessionNotValidRuntimeException extends RuntimeException {
    public SessionNotValidRuntimeException(String message){
        super(message);
    }
}
