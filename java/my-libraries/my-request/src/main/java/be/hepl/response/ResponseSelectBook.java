package src.main.java.be.hepl.response;

import be.hepl.model.entity.Book;

import java.util.ArrayList;

public class ResponseSelectBook implements Response {
    private ArrayList<Book> books;

    public ResponseSelectBook() {}

    public ResponseSelectBook(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "ResponseSelectBook{" + "books=" + books + '}';
    }
}
