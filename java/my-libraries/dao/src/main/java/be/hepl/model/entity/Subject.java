package be.hepl.model.entity;

import java.io.Serializable;

public class Subject implements Entity, Serializable {
    private Integer id;
    private String name;

    public Subject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject() {

    }
    @Override
    public String toString() {
        return "subject [id=" + id + ", name=" + name + "]";
    }

    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    @Override
    public Integer getId() {return id;}
    public String getName() {return name;}

    //***************************************************
    //*********************** SET ***********************
    //***************************************************
    public void setId(Integer id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}
