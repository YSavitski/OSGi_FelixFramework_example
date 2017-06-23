package com.packtpub.felix.bookshelf.service.tui;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;
import org.apache.felix.service.command.Descriptor;

import java.util.Set;

public interface BookshelfServiceProxy {
    String SCOPE = "book";
    String FUNCTION_SEARCH = "search";
    String[] FUNCTIONS = new String[] {"add", "search"};


    @Descriptor("Search books by author, title, or category")
    Set<Book> search(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("search on attribute: author, title, or category") String attribute,
            @Descriptor("match like (use % at the beginning or end of <like> for wild-card)") String filter)
            throws InvalidCredentialException;

    Set<Book> search(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("search on attribute: rating") String attribute,
            @Descriptor("lower rating limit (inclusive)") int lower,
            @Descriptor("upper rating limit (inclusive)") int upper)
            throws InvalidCredentialException;

    @Descriptor("Add new book")
    String add(@Descriptor("username") String username,
                      @Descriptor("password") String password,
                      @Descriptor("ISBN") String isbn,
                      @Descriptor("Title") String title,
                      @Descriptor("Author") String author,
                      @Descriptor("Category") String category,
                      @Descriptor("Rating (0..10)") int rating)
            throws InvalidCredentialException,
            BookAlreadyExistsException, InvalidBookException;
}
