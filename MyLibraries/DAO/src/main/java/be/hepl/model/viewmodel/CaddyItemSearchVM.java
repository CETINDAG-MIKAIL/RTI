package be.hepl.model.viewmodel;

import be.hepl.model.entity.Book;
import be.hepl.model.entity.Caddy;

public class CaddyItemSearchVM {
    private Integer id;
    private Integer caddyId;
    private Integer bookId;

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

    public Integer getCaddyId() {
        return caddyId;
    }

    public void setCaddyId(Integer caddyId) {
        this.caddyId = caddyId;
    }
}
