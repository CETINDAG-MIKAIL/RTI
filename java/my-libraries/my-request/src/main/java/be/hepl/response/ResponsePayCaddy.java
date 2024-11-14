package be.hepl.response;

public class ResponsePayCaddy implements Response {
    private boolean success;

    public ResponsePayCaddy() {}

    public ResponsePayCaddy(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponsePayCaddy{" +
                "success=" + success +
                '}';
    }
}
