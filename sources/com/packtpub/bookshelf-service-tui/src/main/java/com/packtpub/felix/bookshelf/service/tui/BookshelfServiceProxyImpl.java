package com.packtpub.felix.bookshelf.service.tui;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.service.api.BookShelfService;
import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
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

    @Property(name = "osgi.command.function", value = FUNCTION_SEARCH)
    String[] gogoFunctions;

    private enum SearchFilters {
        TITLE,
        AUTHOR,
        CATEGORY,
        RATING
    }

    public BookshelfServiceProxyImpl() {
    }

    @Override
    public Set<Book> search(String username, String password, String attribute, String filter) throws InvalidCredentialException {
        BookShelfService service = lookupService();
        String sessionID = service.login(username, password.toCharArray());
        Set<String> isbnSet;
        if("author".equals(attribute)){
            isbnSet = service.searchBooksByAuthor(sessionID, attribute);
        }
        else if(attribute.toUpperCase().equals(SearchFilters.TITLE)){
            isbnSet = service.searchBooksByAuthor(sessionID, attribute);
        }
        else if(attribute.toUpperCase().equals(SearchFilters.CATEGORY)){
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
    public Set<Book> search(String username, String password, String attribute, int lower, int upper) throws InvalidCredentialException {
        if(!attribute.toUpperCase().equals(SearchFilters.RATING)){
            throw new RuntimeException(
                    "Invalid attribute, expecting 'rating' got '" + attribute + "'");
        }
        BookShelfService service = lookupService();
        String sessionID = service.login(username, password.toCharArray());
        Set<String> isbnSet = service.searchBooksByRating(sessionID, lower, upper);

        return getBooks(sessionID, service, isbnSet);
    }

    @Override
    public String add(String username, String password, String isbn, String title, String author, String category, int rating) throws InvalidCredentialException, BookAlreadyExistsException, InvalidBookException {
        BookShelfService service = lookupService();
        String sessionId = service.login(
                username, password.toCharArray());
        service.addBook(sessionId, isbn, title, author, category, rating);
        return isbn;
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
