package be.hepl.model.entity;

public class CaddyItem {
    private Integer id;
    private Integer caddyId;
    private Integer bookId;
    private Integer quantity;

    public CaddyItem() {

    }

    public CaddyItem(Integer id, Integer caddyId, Integer bookId, Integer quantity) {
        this.id = id;
        this.caddyId = caddyId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCaddyId() {
        return caddyId;
    }

    public void setCaddyId(Integer caddyId) {
        this.caddyId = caddyId;
    }
}
