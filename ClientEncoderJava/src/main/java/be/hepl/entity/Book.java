package be.hepl.entity;

public class Book {
    Integer id;
    String title;
    String author;
    String subject;
    String isbn;
    Integer pages;
    Integer year;
    Float price;
    Integer stock;

    public Book(String title, String author, String subject, String isbn, Integer pages, Integer year, Float price) {
        this.id = -1;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.isbn = isbn;
        this.pages = pages;
        this.year = year;
        this.price = price;
    }

    public Book(Integer id, String title, String author, String subject, String isbn, Integer pages, Integer year, Float price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.isbn = isbn;
        this.pages = pages;
        this.year = year;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
