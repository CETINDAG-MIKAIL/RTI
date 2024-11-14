package be.hepl.response;

public class ResponseEmptyCaddy implements Response {
    private boolean success;

    public ResponseEmptyCaddy() {}

    public ResponseEmptyCaddy(boolean success) {
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
        return "ResponseEmptyCaddy{" +
                "success=" + success +
                '}';
    }
}
