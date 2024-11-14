package be.hepl.response;

public class ResponseDeleteCaddyItem implements Response {
    private boolean success;

    public ResponseDeleteCaddyItem() {}

    public ResponseDeleteCaddyItem(boolean success) {
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
        return "ResponseDeleteCaddyItem{" +
                "success=" + success +
                '}';
    }
}
