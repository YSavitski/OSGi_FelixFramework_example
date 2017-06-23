package com.packtpub.felix.bookshelf.service.impl;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.inventory.api.model.BookInventory;
import com.packtpub.felix.bookshelf.inventory.api.model.MutableBook;
import com.packtpub.felix.bookshelf.service.api.BookShelfService;
import com.packtpub.felix.bookshelf.service.exceptions.BookInventoryNotRegisteredRuntimeException;
import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;
import com.packtpub.felix.bookshelf.service.exceptions.SessionNotValidRuntimeException;
/*
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
*/

import java.util.*;
import java.util.stream.Collectors;


public class BookShelfServiceImpl implements BookShelfService {
    private Map<String, String> library = new HashMap<>();
    private String session;
    //private BundleContext context;
    private BookInventory inventory;

    public BookShelfServiceImpl() {
    }

    /*public BookShelfServiceImpl(BundleContext context){
        this.context = context;
    }*/

    public String login(String username, char[] password) throws InvalidCredentialException {
        if("admin".equals(username) && Arrays.equals(password, "admin".toCharArray())){
            this.session = Long.toString(System.currentTimeMillis());
            return this.session;
        } else {
            throw new InvalidCredentialException(username);
        }
    }

    public void logout(String sessionId) {
        checkSession(sessionId);
        this.session = null;
    }

    public boolean sessionIsValid(String sessionId) {
        return sessionId!=null && sessionId.equals(this.session);
    }

    public Set<String> getGroups(String sessionId) {
        return null;
    }

    public void addBook(String session, String isbn, String title, String author, String category, int rating) throws BookAlreadyExistsException, InvalidBookException {
        checkSession(session);
        //test realisation
        library.put(isbn, author);
    }

    public void modifyBookCategory(String session, String isbn, String category) throws BookNotFoundException, InvalidBookException {

    }

    public void modifyBookRating(String session, String isbn, int rating) throws BookNotFoundException, InvalidBookException {

    }

    public void removeBook(String session, String isbn) throws BookNotFoundException {

    }

    public Book getBook(String session, String isbn) throws BookNotFoundException {
        checkSession(session);
        BookInventory inventory = lookupBookInventory();
        return inventory.loadBook(isbn);
    }

    public MutableBook getBookForEdit(String session, String isbn) throws BookNotFoundException {
        checkSession(session);
        BookInventory inventory = lookupBookInventory();
        return inventory.loadBookForEdit(isbn);

    }

    public Set<String> searchBooksByCategory(String session, String categoryLike) {
        return null;
    }

    public Set<String> searchBooksByAuthor(String session, String authorLike) {
        checkSession(session);

        //test implementation
        String checkedAuthor = authorLike.replaceAll("%","");
        Set<String> results = new HashSet<>();
        return library.entrySet().stream().
                filter(entry -> entry.getValue().contains(checkedAuthor)).
                collect(Collectors.toMap(p->p.getKey(), p->p.getValue())).
                keySet();
    }

    public Set<String> searchBooksByTitle(String session, String titleLike) {
        return null;
    }

    public Set<String> searchBooksByRating(String session, int ratingLower, int ratingUpper) {
        return null;
    }

    public void startedBook(String session, String isbn) throws BookNotFoundException {

    }

    public void finishedBook(String session, String isbn) throws BookNotFoundException {

    }

    public List<String> getNotStartedBooks() {
        return null;
    }

    public List<String> getUnfinishedBooks() {
        return null;
    }

    protected void checkSession(String sessionId){
        if(!sessionIsValid(sessionId)){
            throw new SessionNotValidRuntimeException(sessionId);
        }
    }

    private BookInventory lookupBookInventory() {
        /*String className = BookInventory.class.getName();
        ServiceReference ref = this.context.getServiceReference(className);
        if(ref == null){
            throw new BookInventoryNotRegisteredRuntimeException(className);
        } else {
            return (BookInventory) this.context.getService(ref);
        }*/
        return this.inventory;
    }
}
