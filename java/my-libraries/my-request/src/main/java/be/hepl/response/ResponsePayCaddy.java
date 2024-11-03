package src.main.java.be.hepl.response;

public class ResponsePayCaddy {
    private boolean success;

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
