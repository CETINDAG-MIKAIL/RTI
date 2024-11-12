package be.hepl.request;

public class RequestClient implements Request {
    private String lastName;
    private String firstName;
    private boolean isNewClient;

    public RequestClient() {}

    public RequestClient(String lastName, String firstName, boolean isNewClient) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.isNewClient = isNewClient;
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

    public boolean isNewClient() {
        return isNewClient;
    }

    public void setNewClient(boolean newClient) {
        isNewClient = newClient;
    }

    @Override
    public String toString() {
        return "RequestClient{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", isNewClient=" + isNewClient +
                '}';
    }
}
