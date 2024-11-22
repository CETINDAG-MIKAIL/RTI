package be.hepl.response;

public class ResponseSuccess implements Response {
    private boolean success;

    public ResponseSuccess() {}

    public ResponseSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
