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
    private Integer maxPage_count;
    private Integer minPage_count;
    private Integer minStock_quantity;
    private float minPrice;
    private float maxPrice;
    private Integer minPublish_year;
    private Integer maxPublish_year;



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

    public Integer getMaxPublish_year() {
        return maxPublish_year;
    }

    public void setMaxPublish_year(Integer maxPublish_year) {
        this.maxPublish_year = maxPublish_year;
    }

    public Integer getMinPublish_year() {
        return minPublish_year;
    }

    public void setMinPublish_year(Integer minPublish_year) {
        this.minPublish_year = minPublish_year;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMinStock_quantity() {
        return minStock_quantity;
    }

    public void setMinStock_quantity(Integer minStock_quantity) {
        this.minStock_quantity = minStock_quantity;
    }

    public Integer getMinPage_count() {
        return minPage_count;
    }

    public void setMinPage_count(Integer minPage_count) {
        this.minPage_count = minPage_count;
    }

    public Integer getMaxPage_count() {
        return maxPage_count;
    }

    public void setMaxPage_count(Integer maxPage_count) {
        this.maxPage_count = maxPage_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
