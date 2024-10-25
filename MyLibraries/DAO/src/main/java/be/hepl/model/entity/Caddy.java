package be.hepl.model.entity;

import java.time.LocalDate;

public class Caddy {
    private Integer id;
    private Client client;
    private LocalDate date;
    private Integer amount;
    private boolean payed;

    public Caddy() {

    }

    public Caddy(Integer id, Client client, LocalDate date, Integer amount, boolean payed) {
        this.id = id;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
