package be.hepl.model.entity;

import java.time.LocalDate;

public class Caddy {
    private Integer id;
    private Client client;
    private LocalDate date;
    private Integer amount;             // ce sera calculé, donc pas stocké dans la BD
    private Boolean payed;

    public Caddy() {

    }

    public Caddy(Integer id, Client client, LocalDate date, Integer amount, Boolean payed) {
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

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }
}
