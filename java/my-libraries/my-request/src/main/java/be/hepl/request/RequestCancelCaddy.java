package src.main.java.be.hepl.request;

public class RequestCancelCaddy implements Request {
    private int idClient;

    public RequestCancelCaddy() {}

    public RequestCancelCaddy(int idClient) {
        this.idClient = idClient;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    @Override
    public String toString() {
        return "RequestCancelCaddy{" +
                "idClient=" + idClient +
                '}';
    }
}
