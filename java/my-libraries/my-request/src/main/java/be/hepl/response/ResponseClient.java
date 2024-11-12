package be.hepl.response;

public class ResponseClient implements Response {
    private Integer id;

    public ResponseClient() {}

    public ResponseClient(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public boolean isValide() {
        return id != null && id > 0;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ResponseLogin [id=" + id + "]";
    }
}
