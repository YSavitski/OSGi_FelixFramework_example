package com.packtpub.felix.bookshelf.service.impl;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.service.api.BookShelfService;
import com.packtpub.felix.bookshelf.service.exceptions.InvalidCredentialException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Set;

public class BookshelfServiceImplActivator implements BundleActivator {

    private ServiceRegistration registration = null;

    public void start(BundleContext bundleContext) throws Exception {
        this.registration = bundleContext.registerService(BookShelfService.class.getName(),
                new BookShelfServiceImpl(bundleContext), null);

        testService(bundleContext);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        if(this.registration != null){
            bundleContext.ungetService(registration.getReference());
        }
    }

    private void testService(BundleContext bundleContext) {
        String className = BookShelfService.class.getName();
        ServiceReference ref = bundleContext.getServiceReference(className);
        if (ref==null) {
            throw new RuntimeException(
                    "Service not registered: " + className);
        }
        BookShelfService service = (BookShelfService) bundleContext.getService(ref);

        // authenticate and get session
        String sessionId;
        try
        {
            System.out.println("\nSigning in. . .");
            sessionId = service.login("admin", "admin".toCharArray());
        }
        catch (InvalidCredentialException e)
        {
            e.printStackTrace();
            return;
        }

        // add a few books
        try
        {
            System.out.println("\nAdding books. . .");
            service.addBook(sessionId, "123-4567890100", "Book 1 Title", "John Doe", "Group 1", 0);
            service.addBook(sessionId, "123-4567890101", "Book 2 Title", "Will Smith", "Group 1", 0);
            service.addBook(sessionId, "123-4567890200", "Book 3 Title", "John Doe", "Group 2", 0);
            service.addBook(sessionId, "123-4567890201", "Book 4 Title", "Jane Doe", "Group 2", 0);
        }
        catch (BookAlreadyExistsException | InvalidBookException e)
        {
            e.printStackTrace();
            return;
        }

        // test search
        String authorLike = "%Doe";
        System.out.println("Search for book with author like: " + authorLike);
        Set<String> searchResult = service.searchBooksByAuthor(sessionId, authorLike);
        searchResult.stream().forEach(isbn -> {
            try {
                System.out.println(service.getBook(sessionId, isbn));
            } catch (BookNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
