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
    private String lastName;
    private String firstName;
    private String birthDate;

    public Author(Integer authorId, String lastName, String firstName, String birthDate) {
        this.id = authorId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
    }

    public Author() {

    }
    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }

    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    @Override
    public Integer getId() {return id;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getBirthDate() {return birthDate;}




    //***************************************************
    //*********************** SET ***********************
    //***************************************************


    public void setBirthDate(String birthDate) {this.birthDate = birthDate;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public void setId(Integer id) { this.id = id;}
}
