package be.hepl.response;

public class ResponseEmployee implements Response {
    private boolean success;

    public ResponseEmployee() {}

    public ResponseEmployee(boolean success) {
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
