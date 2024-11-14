package be.hepl.request;

public class RequestEmptyCaddy implements Request {
    private int idClient;

    public RequestEmptyCaddy() {}

    public RequestEmptyCaddy(int idClient) {
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
        return "RequestEmptyCaddy{" +
                "idClient=" + idClient +
                '}';
    }
}
