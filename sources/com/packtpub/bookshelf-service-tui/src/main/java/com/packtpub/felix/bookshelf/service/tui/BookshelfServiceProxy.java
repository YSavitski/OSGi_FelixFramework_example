package com.packtpub.felix.bookshelf.service.tui;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;
import org.apache.felix.service.command.Descriptor;

import java.util.Set;

public interface BookshelfServiceProxy {
    String SCOPE = "book";
    String FUNCTIONS_STR = "{add, search, getBooksCount}";


    Set<Book> search(String username, String password, String attribute, String filter) throws InvalidCredentialException;

    Set<Book> search(String username, String password, String attribute, int lower, int upper) throws InvalidCredentialException;


    String add(String username, String password, String isbn, String title, String author, String category, int rating)
            throws InvalidCredentialException,
            BookAlreadyExistsException, InvalidBookException;

    int getBooksCount(String username, String password) throws InvalidCredentialException;
}
