package be.hepl.response;

public class ResponseSecure implements Response {
    private Response response;
    private byte[] data;

    public ResponseSecure() {}

    public ResponseSecure (Response response, byte[] data) {
        this.response = response;
        this.data = data;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
