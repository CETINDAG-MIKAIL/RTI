package src.main.java.be.hepl.request;

public class RequestPayCaddy {
    private int idClient;

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
