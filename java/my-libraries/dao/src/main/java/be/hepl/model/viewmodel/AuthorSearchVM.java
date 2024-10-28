package be.hepl.model.viewmodel;

public class AuthorSearchVM {
    private Integer id;
    private String lastName;
    private String firstName;
    private String birthDate;


    public AuthorSearchVM() {}

    //***************************************************
    //*********************** GET ***********************
    //***************************************************
    public Integer getId() { return id; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getBirthDate() { return birthDate; }

    //***************************************************
    //*********************** SET ***********************
    //***************************************************

    public void setId(Integer id) { this.id = id; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

}
