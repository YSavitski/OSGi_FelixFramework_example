package com.packtpub.felix.bookshelf.service.tui;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.service.api.BookShelfService;
import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;
import org.apache.felix.ipojo.annotations.*;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component(name="BookshelfServiceProxy")
@Provides
public class BookshelfServiceProxyImpl implements BookshelfServiceProxy {

    @Requires
    private BookShelfService bookShelfService;

    @Property(name="osgi.command.scope", value = SCOPE)
    String gogoScope;

    @Property(name = "osgi.command.function", value = FUNCTIONS_STR)
    String[] gogoFunctions;

    private enum SearchFilters {
        TITLE("title"), CATEGORY("category"), AUTHOR("author"), RATING("rating");

        private String message;
        private SearchFilters(String message){
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public BookshelfServiceProxyImpl() {
    }

    @Override
    @Descriptor("Search books by author, title, or category")
    public Set<Book> search(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("search on attribute: author, title, or category") String attribute,
            @Descriptor("match like (use % at the beginning or end of <like> for wild-card)") String filter)
            throws InvalidCredentialException {
        BookShelfService service = lookupService();
        String sessionID = service.login(username, password.toCharArray());
        Set<String> isbnSet;
        if(/*"author".equals(attribute)*/attribute.toLowerCase().equals(SearchFilters.AUTHOR)){
            isbnSet = service.searchBooksByAuthor(sessionID, attribute);
        }
        else if(attribute.toLowerCase().equals(SearchFilters.TITLE)){
            isbnSet = service.searchBooksByAuthor(sessionID, attribute);
        }
        else if(attribute.toLowerCase().equals(SearchFilters.CATEGORY)){
            isbnSet = service.searchBooksByAuthor(sessionID, attribute);
        }
        else {
            throw new RuntimeException(
                    "Invalid attribute, expecting one of " +
                            Arrays.asList(SearchFilters.values()) +
                            " got '"+attribute+"'");
        }

        return getBooks(sessionID, service, isbnSet);
    }

    @Override
    @Descriptor("Search books by rating")
    public Set<Book> search(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("search on attribute: rating") String attribute,
            @Descriptor("lower rating limit (inclusive)") int lower,
            @Descriptor("upper rating limit (inclusive)") int upper)
            throws InvalidCredentialException {
        if(!attribute.toLowerCase().equals(SearchFilters.RATING)){
            throw new RuntimeException(
                    "Invalid attribute, expecting 'rating' got '" + attribute + "'");
        }
        BookShelfService service = lookupService();
        String sessionID = service.login(username, password.toCharArray());
        Set<String> isbnSet = service.searchBooksByRating(sessionID, lower, upper);

        return getBooks(sessionID, service, isbnSet);
    }

    @Override
    @Descriptor("Add new book")
    public String add(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("ISBN") String isbn,
            @Descriptor("Title") String title,
            @Descriptor("Author") String author,
            @Descriptor("Category") String category,
            @Descriptor("Rating (0..10)") int rating)
            throws InvalidCredentialException,
            BookAlreadyExistsException, InvalidBookException {
        BookShelfService service = lookupService();
        String sessionId = service.login(
                username, password.toCharArray());
        service.addBook(sessionId, isbn, title, author, category, rating);
        return isbn;
    }

    @Override
    @Descriptor("Get books oount")
    public int getBooksCount(
            @Descriptor("username") String username,
            @Descriptor("password") String password)
            throws InvalidCredentialException {
        BookShelfService service = lookupService();
        String sessionID = service.login(username, password.toCharArray());
        return service.getBooksCount(sessionID);
    }

    protected BookShelfService lookupService(){
        return this.bookShelfService;
    }

    private Set<Book> getBooks(String sessionId, BookShelfService service, Set<String> isbnSet){
        Set<Book> books = new HashSet<>();
        isbnSet.stream().forEach(isbn -> {
            Book book = null;
            try {
                book = service.getBook(sessionId, isbn);
                if(book != null) books.add(book);
            } catch (BookNotFoundException e) {
                System.err.println("ISBN " + isbn +
                        " referenced but not found");
            }
        });
        return books;
    }
}
