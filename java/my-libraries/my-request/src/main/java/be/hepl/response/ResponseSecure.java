package be.hepl.response;

public class ResponseSecure implements Response {
    private Response response;
    private byte[] requestEncrypted;
    private byte[] sel;
    private byte[] sessionsKey;
    private byte[] hmac;

    public ResponseSecure() {}

    public ResponseSecure (Response response, byte[] requestEncrypted, byte[] sel, byte[] sessionsKey, byte[] hmac) {
        this.response = response;
        this.requestEncrypted = requestEncrypted;
        this.sel = sel;
        this.sessionsKey = sessionsKey;
        this.hmac = hmac;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public byte[] getRequestEncrypted() {
        return requestEncrypted;
    }

    public void setRequestEncrypted(byte[] requestEncrypted) {
        this.requestEncrypted = requestEncrypted;
    }

    public byte[] getSel() {
        return sel;
    }

    public void setSel(byte[] sel) {
        this.sel = sel;
    }

    public byte[] getSessionsKey() {
        return sessionsKey;
    }

    public void setSessionsKey(byte[] sessionsKey) {
        this.sessionsKey = sessionsKey;
    }

    public byte[] getHmac() {
        return hmac;
    }

    public void setHmac(byte[] hmac) {
        this.hmac = hmac;
    }
}
