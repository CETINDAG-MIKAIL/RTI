package be.hepl.entity;

public class Employee {
    String login;
    String password;

    public Employee(String login, String password) {
        login = login;
        password = password;
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
}
