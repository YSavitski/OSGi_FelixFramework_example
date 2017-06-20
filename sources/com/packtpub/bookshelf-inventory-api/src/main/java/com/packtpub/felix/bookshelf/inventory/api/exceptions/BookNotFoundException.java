package com.packtpub.felix.bookshelf.inventory.api.exceptions;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message){
        super("Book " + message + " not found");
    }
}
