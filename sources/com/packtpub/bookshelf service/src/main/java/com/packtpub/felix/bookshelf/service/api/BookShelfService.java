package com.packtpub.felix.bookshelf.service.api;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.inventory.api.model.MutableBook;

import java.util.List;
import java.util.Set;

public interface BookShelfService extends Authentication {
    Set<String> getCategories(String sessionId);

    int getBooksCount(String session);

    void addBook(String session, String isbn, String title, String author, String category, int rating)
            throws BookAlreadyExistsException, InvalidBookException;

    void modifyBookCategory(String session, String isbn, String category) throws BookNotFoundException, InvalidBookException;

    void modifyBookRating(String session, String isbn, int rating) throws BookNotFoundException, InvalidBookException;

    void removeBook(String session, String isbn) throws BookNotFoundException;

    Book getBook(String session, String isbn) throws BookNotFoundException;
    MutableBook getBookForEdit(String session, String isbn) throws BookNotFoundException;

    Set<String> searchBooksByCategory(String session, String categoryLike);

    Set<String> searchBooksByAuthor(String session, String authorLike);

    Set<String> searchBooksByTitle(String session, String titleLike);

    Set<String> searchBooksByRating(String session, int ratingLower, int ratingUpper);

    void startedBook(String session, String isbn) throws BookNotFoundException;
    void finishedBook(String session, String isbn) throws BookNotFoundException;
    List<String> getNotStartedBooks();
    List<String> getUnfinishedBooks();
}
