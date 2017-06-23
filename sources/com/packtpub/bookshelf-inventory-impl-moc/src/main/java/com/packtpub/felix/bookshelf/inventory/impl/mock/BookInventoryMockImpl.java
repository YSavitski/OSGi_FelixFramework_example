package com.packtpub.felix.bookshelf.inventory.impl.mock;

import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.model.Book;
import com.packtpub.felix.bookshelf.inventory.api.model.BookInventory;
import com.packtpub.felix.bookshelf.inventory.api.model.MutableBook;

import java.util.*;

public class BookInventoryMockImpl implements BookInventory {
    public static final String DEFAULT_CATEGORY = "default";
    private Map<String, MutableBook> booksByISBN = new HashMap<>();
    private Map<String, Integer> categories = new HashMap<>();

    @Override
    public Set<String> getCategories() {
        return this.categories.keySet();
    }



    @Override
    public MutableBook createBook(String isbn) throws BookAlreadyExistsException {
        return new MutableBookImpl(isbn);
    }

    @Override
    public MutableBook loadBookForEdit(String isbn) throws BookNotFoundException {
        MutableBook book = this.booksByISBN.get(isbn);
        if (book == null){
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    @Override
    public String storeBook(MutableBook mutableBook) throws InvalidBookException {
        String isbn = mutableBook.getIsbn();
        if(isbn == null){
            throw new InvalidBookException("ISBN is not set");
        }

        this.booksByISBN.put(isbn, mutableBook);
        String category = mutableBook.getCategory();
        if(category == null){
            category = DEFAULT_CATEGORY;
        }
        if (this.categories.containsKey(category)){
            int count = this.categories.get(category);
            this.categories.put(category, count++);
        } else {
            this.categories.put(category, 1);
        }
        return isbn;
    }

    @Override
    public Book loadBook(String isbn) throws BookNotFoundException {
        return loadBookForEdit(isbn);
    }

    @Override
    public void removeBook(String isbn) throws BookNotFoundException {
        Book book = this.booksByISBN.remove(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }

        String category = book.getCategory();
        int count = this.categories.get(category);
        if(count == 1){
            this.categories.remove(category);
        } else {
            this.categories.put(category, count-1);
        }
    }

    @Override
    public Set<String> searchBooks(Map<SearchCriteria, String> criteria) {
        LinkedList<Book> books = new LinkedList<>();
        books.addAll(this.booksByISBN.values());

        criteria.entrySet().stream().forEach(criterion -> {
            Iterator<Book> iterator = books.iterator();
            while (iterator.hasNext()){
                Book book = iterator.next();
                switch (criterion.getKey()){
                    case AUHTOR_LIKE:
                        if(!checkStringMatch(book.getAuthor(), criterion.getValue())){
                            iterator.remove();
                            continue;
                        }
                        break;
                    case ISBN_LIKE:
                        if(!checkStringMatch(book.getIsbn(), criterion.getValue())){
                            iterator.remove();
                            continue;
                        }
                        break;
                    case TITLE_LIKE:
                        if(!checkStringMatch(book.getTitle(), criterion.getValue())){
                            iterator.remove();
                            continue;
                        }
                        break;
                    case CATEGORY_LIKE:
                        if(!checkStringMatch(book.getCategory(), criterion.getValue())){
                            iterator.remove();
                            continue;
                        }
                        break;
                    case GRADE_GT:
                        if (!checkIntegerGreater(
                                book.getRating(), criterion.getValue()))
                        {
                            iterator.remove();
                            continue;
                        }
                        break;
                    case GRADE_LT:
                        if (!checkIntegerSmaller(
                                book.getRating(), criterion.getValue()))
                        {
                            iterator.remove();
                            continue;
                        }
                        break;
                }
            }
        });

        Set<String> isbn = new HashSet<>();
        books.stream().forEach(book -> isbn.add(book.getIsbn()));
        return isbn;
    }

    @Override
    public int getBooksCount() {
        return booksByISBN.size();
    }

    private boolean checkStringMatch(String attr, String critVal){
        if (attr == null) {
            return false;
        }
        attr = attr.toLowerCase();
        critVal = critVal.toLowerCase();
        boolean startsWith = critVal.startsWith("%");
        boolean endsWith = critVal.endsWith("%");
        if (startsWith && endsWith) {
            if (critVal.length()==1) {
                return true;
            } else {
                return attr.contains(
                        critVal.substring(1, critVal.length() - 1));
            }
        }
        else if (startsWith){
            return attr.endsWith(critVal.substring(1));
        }
        else if (endsWith) {
            return attr.startsWith(
                    critVal.substring(0, critVal.length() - 1));
        }
        else {
            return attr.equals(critVal);
        }
    }

    private boolean checkIntegerGreater(int attr, String critVal)
    {
        int critValInt;
        try {
            critValInt = Integer.parseInt(critVal);
        }
        catch (NumberFormatException e) {
            return false;
        }
        if (attr >= critValInt) {
            return true;
        }
        return false;
    }

    private boolean checkIntegerSmaller(int attr, String critVal)
    {
        int critValInt;
        try {
            critValInt = Integer.parseInt(critVal);
        }
        catch (NumberFormatException e) {
            return false;
        }
        if (attr < critValInt) {
            return true;
        }
        return false;
    }
}
