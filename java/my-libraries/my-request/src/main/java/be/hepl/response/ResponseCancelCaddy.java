package be.hepl.response;

public class ResponseCancelCaddy implements Response {
    private boolean success;

    public ResponseCancelCaddy() {}

    public ResponseCancelCaddy(boolean success) {
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
        return "ResponseAddCaddyItem{" +
                "success=" + success +
                '}';
    }
}
