package be.hepl.response;

public class ResponseByteTable implements Response {
    private byte[] data;

    public ResponseByteTable() {}

    public ResponseByteTable(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
