package be.hepl.model.entity;

public class Client implements Entity {
    private Integer id;
    private String lastName;
    private String firstName;
    private Integer numClient;
    private String address;

    public Client() {
    }

    public Client(Integer id, String lastName, String firstName, Integer numClient, String address) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.numClient = numClient;
        this.address = address;
    }

    @Override
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
