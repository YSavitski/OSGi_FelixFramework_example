package com.packtpub.felix.bookshelf.service.tui;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.service.api.BookShelfService;
import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BookshelfServiceProxy {
    public static final String SCOPE = "book";
    public static final String[] FUNCTIONS = new String[] {"search"};
    private enum SearchFilters {
        TITLE,
        AUTHOR,
        CATEGORY,
        RATING
    }

    private BundleContext context;

    public BookshelfServiceProxy(BundleContext context) {
        this.context = context;
    }

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

    @Descriptor("Search books by rating)")
    public Set<Book> search(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("search on attribute: rating") String attribute,
            @Descriptor("lower rating limit (inclusive)") int lower,
            @Descriptor("upper rating limit (inclusive)") int upper)
            throws InvalidCredentialException {
        if(!attribute.toUpperCase().equals(SearchFilters.RATING)){
            throw new RuntimeException(
                    "Invalid attribute, expecting 'rating' got '" + attribute + "'");
        }
        BookShelfService service = lookupService();
        String sessionID = service.login(username, password.toCharArray());
        Set<String> isbnSet = service.searchBooksByRating(sessionID, lower, upper);

        return getBooks(sessionID, service, isbnSet);
    }

    protected BookShelfService lookupService(){
        ServiceReference reference = context.getServiceReference(BookShelfService.class.getName());
        if (reference == null)
        {
            throw new RuntimeException(
                    "BookshelfService not registered, cannot invoke operation");
        }

        BookShelfService service = (BookShelfService) this.context.getService(reference);
        if (service == null){
            throw new RuntimeException(
                    "BookshelfService not registered, cannot invoke operation");
        }
        return service;
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
