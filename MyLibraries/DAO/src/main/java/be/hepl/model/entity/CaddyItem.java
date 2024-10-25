package be.hepl.model.entity;

public class CaddyItem implements Entity {
    private Integer id;
    private Caddy caddy;
    private Book book;
    private Integer quantity;

    public CaddyItem() {

    }

    public CaddyItem(Integer id, Caddy caddy, Book book, Integer quantity) {
        this.id = id;
        this.caddy = caddy;
        this.book = book;
        this.quantity = quantity;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Caddy getCaddy() {
        return caddy;
    }

    public void setCaddy(Caddy caddy) {
        this.caddy = caddy;
    }
}
