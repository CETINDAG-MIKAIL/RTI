package be.hepl.model.entity;
/*
       typedef struct {
           Integer   id;
           Integer   author_id;    // clé étrangère
           Integer   subject_id;   // clé étrangère
           char  title[100];
           char  isbn[20];
           Integer   page_count;
           Integer   stock_quantity;
           float price;
           Integer   publish_year;
       } BOOK;
   */
public class Book implements Entity{


    private Integer id;
    private Author author;
    private Subject subject;
    private String title;
    private String isbn;
    private Integer pageCount;
    private Integer stockQuantity;
    private float price;
    private Integer publishYear;

    public Book(Integer id, Author author, Subject subject, String title, String isbn, Integer pageCount, Integer stockQuantity, Integer publishYear, float price) {
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

    public Book() {

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
    public float getPrice() { return price; }

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
    public void setPrice(float price) { this.price = price; }


}
