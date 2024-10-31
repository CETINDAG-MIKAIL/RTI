package be.hepl.model.viewmodel;

import be.hepl.model.entity.Author;
import be.hepl.model.entity.Subject;

public class BookSearchVM {

    public BookSearchVM() {}
    private Integer id;
    private Author author;
    private Subject subject;
    private String title;
    private String isbn;
    private Integer maxPageCount;
    private Integer minPageCount;
    private Integer minStockQuantity;
    private Float minPrice;
    private Float maxPrice;
    private Integer minPublishYear;
    private Integer maxPublishYear;



    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getMaxPublishYear() {
        return maxPublishYear;
    }

    public void setMaxPublishYear(Integer maxPublishYear) {
        this.maxPublishYear = maxPublishYear;
    }

    public Integer getMinPublishYear() {
        return minPublishYear;
    }

    public void setMinPublishYear(Integer minPublishYear) {
        this.minPublishYear = minPublishYear;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMinStockQuantity() {
        return minStockQuantity;
    }

    public void setMinStockQuantity(Integer minStockQuantity) {
        this.minStockQuantity = minStockQuantity;
    }

    public Integer getMinPageCount() {
        return minPageCount;
    }

    public void setMinPageCount(Integer minPageCount) {
        this.minPageCount = minPageCount;
    }

    public Integer getMaxPageCount() {
        return maxPageCount;
    }

    public void setMaxPageCount(Integer maxPageCount) {
        this.maxPageCount = maxPageCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
