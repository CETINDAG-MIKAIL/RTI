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
    private Integer page_count;
    private Integer stock_quantity;
    private float price;
    private Integer publish_year;

    public Book(Integer id, Author author, Subject subject, String title, String isbn, Integer pageCount, Integer stockQuantity, Integer publishYear, float price) {
        this.id = id;
        this.author = author;
        this.subject = subject;
        this.title = title;
        this.isbn = isbn;
        this.page_count = pageCount;
        this.stock_quantity = stockQuantity;
        this.publish_year = publishYear;
        this.price = price;
    }

    public Book() {

    }


    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    @Override
    public Integer getId() {return 0;}
    public Author getAuthor() { return author; }
    public Subject getSubject() { return subject; }
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public Integer getPage_count() { return page_count; }
    public Integer getStock_quantity() { return stock_quantity; }
    public Integer getPublish_year() { return publish_year; }
    public float getPrice() { return price; }

    //***************************************************
    //*********************** SET ***********************
    //***************************************************
    public void setId(Integer id) {this.id = id;}
    public void setAuthor(Author author) { this.author = author; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public void setTitle(String title) { this.title = title; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPage_count(Integer page_count) { this.page_count = page_count; }
    public void setStock_quantity(Integer stock_quantity) { this.stock_quantity = stock_quantity; }
    public void setPublish_year(Integer publish_year) { this.publish_year = publish_year; }
    public void setPrice(float price) { this.price = price; }


}
