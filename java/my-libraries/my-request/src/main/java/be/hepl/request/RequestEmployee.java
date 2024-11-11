package src.main.java.be.hepl.request;

public class RequestEmployee implements Request {
    private String login;
    private String password;

    public RequestEmployee() {}

    public RequestEmployee(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestEmployee{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
