package be.hepl.model.viewmodel;

import be.hepl.model.entity.Author;

public class AuthorSearchVM {
    private Integer id;
    private String last_name;
    private String first_name;
    private String birth_date;


    public AuthorSearchVM() {}

    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    public Integer getId() { return id; }
    public String getLast_name() { return last_name; }
    public String getFirst_name() { return first_name; }
    public String getBirth_date() { return birth_date; }

    //***************************************************
    //*********************** SET ***********************
    //***************************************************

    public void setId(Integer id) { this.id = id; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public void setBirth_date(String birth_date) { this.birth_date = birth_date; }

}
