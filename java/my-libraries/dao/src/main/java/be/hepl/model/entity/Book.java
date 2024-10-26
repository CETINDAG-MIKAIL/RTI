package be.hepl.model.entity;
/*
       typedef struct {
           int   id;
           int   author_id;    // clé étrangère
           int   subject_id;   // clé étrangère
           char  title[100];
           char  isbn[20];
           int   page_count;
           int   stock_quantity;
           float price;
           int   publish_year;
       } BOOK;
   */
public class Book implements Entity{
    private int id;
    private Author authorId;
    private Subject subjectId;
    private String title;
    private String author;
    private int stock_quantity;
    private int publish_year;
    private float price;


    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    @Override
    public Integer getId() {return 0;}
    public float getPrice() {return price;}
    public int getPublish_year() {return publish_year;}
    public int getStock_quantity() {return stock_quantity;}
    public String getAuthor() {return author;}
    public String getTitle() {return title;}
    public Subject getSubjectId() {return subjectId;}
    public Author getAuthorId() {return authorId;}



    //***************************************************
    //*********************** SET ***********************
    //***************************************************
    public void setAuthorId(Author authorId) {this.authorId = authorId;}
    public void setSubjectId(Subject subjectId) {this.subjectId = subjectId;}
    public void setAuthor(String author) {this.author = author;}
    public void setStock_quantity(int stock_quantity) {this.stock_quantity = stock_quantity;}
    public void setPrice(float price) {this.price = price;}
    public void setPublish_year(int publish_year) {this.publish_year = publish_year;}
    public void setTitle(String title) {this.title = title;}

}
