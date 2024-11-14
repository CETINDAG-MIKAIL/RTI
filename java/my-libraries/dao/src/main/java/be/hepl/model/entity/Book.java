package be.hepl.model.entity;

import java.io.Serializable;

public class Book implements Entity, Serializable {


    private Integer id;
    private Author author;
    private Subject subject;
    private String title;
    private String isbn;
    private Integer pageCount;
    private Integer stockQuantity;
    private Float price;
    private Integer publishYear;

    public Book(Integer id, Author author, Subject subject, String title, String isbn, Integer pageCount, Integer stockQuantity, Integer publishYear, Float price) {
        this.id = id;
        this.author = author;
        this.subject = subject;
        this.title = title;
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.stockQuantity = stockQuantity;
        this.publishYear = publishYear;
        this.price = price;
    }
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author=" + (author != null ? author : "Unknown") +
                ", subject=" + (subject != null ? subject : "Unknown") +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", pageCount=" + pageCount +
                ", stockQuantity=" + stockQuantity +
                ", publishYear=" + publishYear +
                ", price=" + price +
                '}';
    }
    public Book() {

    }
    public Book(Book otherBook) {
        this.id = otherBook.id;
        this.author = otherBook.author; // Vous pourriez vouloir créer une copie de l'auteur si nécessaire
        this.subject = otherBook.subject; // Idem pour le sujet
        this.title = otherBook.title;
        this.isbn = otherBook.isbn;
        this.pageCount = otherBook.pageCount;
        this.publishYear = otherBook.publishYear;
        this.price = otherBook.price;
        this.stockQuantity = otherBook.stockQuantity; // Vous pouvez initialiser la quantité à une valeur spécifique
    }


    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    @Override
    public Integer getId() {return id;}
    public Author getAuthor() { return author; }
    public Subject getSubject() { return subject; }
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public Integer getPageCount() { return pageCount; }
    public Integer getStockQuantity() { return stockQuantity; }
    public Integer getPublishYear() { return publishYear; }
    public Float getPrice() { return price; }

    //***************************************************
    //*********************** SET ***********************
    //***************************************************
    public void setId(Integer id) {this.id = id;}
    public void setAuthor(Author author) { this.author = author; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public void setTitle(String title) { this.title = title; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public void setPrice(Float price) { this.price = price; }


}
