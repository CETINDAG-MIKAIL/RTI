package be.hepl.request;

public class RequestPayCaddyWithVisa implements Request {
    private int idClient;
    private int numVisa;
    private String nameVisa;

    public RequestPayCaddyWithVisa() {}

    public RequestPayCaddyWithVisa(int idClient, int numVisa, String nameVisa) {
        this.idClient = idClient;
        this.numVisa = numVisa;
        this.nameVisa = nameVisa;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getNumVisa() {
        return numVisa;
    }

    public void setNumVisa(int numVisa) {
        this.numVisa = numVisa;
    }

    public String getNameVisa() {
        return nameVisa;
    }

    public void setNameVisa(String nameVisa) {
        this.nameVisa = nameVisa;
    }
}
