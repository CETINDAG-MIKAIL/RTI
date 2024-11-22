package be.hepl.request;

public class RequestDigest implements Request {
    private int clientNumber;
    private byte[] digest;

    public RequestDigest(int clientNumber, byte[] digest) {
        this.clientNumber = clientNumber;
        this.digest = digest;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public byte[] getDigest() {
        return digest;
    }
}
