package be.hepl.model.entity;
/*
    typedef struct {
        int  id;
        char last_name[20];
        char first_name[20];
        char birth_date[20];
    } AUTHOR;
*/
public class Author implements Entity{


    private Integer id;
    private String last_name;
    private String first_name;
    private String birth_date;

    public Author() {
        this.id = null;
        this.last_name = "";
        this.first_name = "";
        this.birth_date = "";
    }
    public Author(Integer id, String last_name, String first_name, String birth_date) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.birth_date = birth_date;
    }
    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    @Override
    public Integer getId() {return 0;}
    public String getFirst_name() {return first_name;}
    public String getLast_name() {return last_name;}
    public String getBirth_date() {return birth_date;}

    //***************************************************
    //*********************** SET ***********************
    //***************************************************
    public void setId(Integer id) {this.id = id;}
    public void setBirth_date(String birth_date) {this.birth_date = birth_date;}
    public void setLast_name(String last_name) {this.last_name = last_name;}
    public void setFirst_name(String first_name) {this.first_name = first_name;}
}
