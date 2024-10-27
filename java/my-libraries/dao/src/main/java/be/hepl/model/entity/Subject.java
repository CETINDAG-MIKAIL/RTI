package be.hepl.model.entity;
/*

    typedef struct {
        int  id;
        char name[20];
    } SUBJECT;

*/
public class Subject implements Entity {
    private Integer id;
    private String name;

    public Subject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject() {

    }

    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    @Override
    public Integer getId() {return 0;}
    public String getName() {return name;}

    //***************************************************
    //*********************** SET ***********************
    //***************************************************
    public void setId(Integer id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}
