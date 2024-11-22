package be.hepl.request;

import be.hepl.response.Response;

import java.io.Serializable;

public class RequestSecure implements Serializable {
    private Request request;
    private byte[] data;

    public RequestSecure() {}

    public RequestSecure (Request request, byte[] data) {
        this.request = request;
        this.data = data;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
