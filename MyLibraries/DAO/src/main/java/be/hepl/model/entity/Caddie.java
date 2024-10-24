package be.hepl.model.entity;

import java.time.LocalDate;

public class Caddie {
    private Integer id;
    private Integer clientId;
    private LocalDate date;
    private Integer amount;
    private boolean payed;

    public Caddie() {

    }

    public Caddie(Integer id, Integer clientId, LocalDate date, Integer amount, boolean payed) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.amount = amount;
        this.payed = payed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }
}
