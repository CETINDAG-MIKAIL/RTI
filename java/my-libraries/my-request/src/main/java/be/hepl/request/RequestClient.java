package src.main.java.be.hepl.request;

public class RequestClient implements Request {
    private String lastName;
    private String firstName;

    public RequestClient(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Request{lastName='" + lastName + "', firstName='" + firstName + "'}";
    }
}
