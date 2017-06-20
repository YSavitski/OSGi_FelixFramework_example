package com.packtpub.felix.bookshelf.inventory.impl.mock;

import com.packtpub.felix.bookshelf.inventory.api.model.MutableBook;

public class MutableBookImpl implements MutableBook {
    private String isbn;
    private String author;
    private String title;
    private String category;
    private int rating;

    public MutableBookImpl(String isbn) {
        setIsbn(isbn);
    }

    @Override
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String getIsbn() {
        return this.isbn;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public int getRating() {
        return this.rating;
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(getCategory()).append(": ");
        sBuilder.append(getTitle()).append(" from ").append(getAuthor());
        sBuilder.append(" [").append(getRating()).append("]");
        return sBuilder.toString();
    }
}
