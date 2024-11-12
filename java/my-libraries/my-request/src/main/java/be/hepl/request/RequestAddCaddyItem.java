package be.hepl.request;

public class RequestAddCaddyItem implements Request {
    private int idBook;
    private int quantity;

    public RequestAddCaddyItem() {}

    public RequestAddCaddyItem(int idBook, int quantity) {
        this.idBook = idBook;
        this.quantity = quantity;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "RequestAddCaddyItem{" +
                "idBook=" + idBook +
                ", quantity=" + quantity +
                '}';
    }
}
