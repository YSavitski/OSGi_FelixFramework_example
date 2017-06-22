package com.packtpub.felix.bookshelf.service.exceptions;

public class BookInventoryNotRegisteredRuntimeException extends RuntimeException {
    public BookInventoryNotRegisteredRuntimeException(String className){
        super(className);
    }
}
