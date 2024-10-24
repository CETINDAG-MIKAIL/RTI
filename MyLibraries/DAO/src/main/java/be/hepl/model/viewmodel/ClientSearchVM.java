package be.hepl.model.viewmodel;

public class ClientSearchVM {
    private Integer id;
    private String lastName;
    private String firstName;
    private Integer numClient;
    private String containAddress;

    public ClientSearchVM() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getNumClient() {
        return numClient;
    }

    public void setNumClient(Integer numClient) {
        this.numClient = numClient;
    }

    public String getContainAddress() {
        return containAddress;
    }

    public void setContainAddress(String containAddress) {
        this.containAddress = containAddress;
    }
}
