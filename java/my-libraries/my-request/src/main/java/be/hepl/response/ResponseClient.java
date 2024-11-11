package src.main.java.be.hepl.response;

public class ResponseClient implements Response {
    private Integer id;

    public ResponseClient() {}

    public ResponseClient(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ResponseLogin [id=" + id + "]";
    }
}
