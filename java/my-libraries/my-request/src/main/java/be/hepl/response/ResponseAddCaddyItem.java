package be.hepl.response;

import be.hepl.model.entity.Caddy;

public class ResponseAddCaddyItem implements Response {
    private boolean success;

    public ResponseAddCaddyItem() {}

    public ResponseAddCaddyItem(boolean success) {
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
