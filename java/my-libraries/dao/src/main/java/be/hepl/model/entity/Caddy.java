package be.hepl.model.entity;

import java.time.LocalDate;

public class Caddy implements Entity {
    private Integer id;
    private Client client;
    private LocalDate date;
    private Double amount;             // ce sera calculé, donc pas stocké dans la BD
    private Boolean payed;

    public Caddy() {

    }

    public Caddy(Integer id, Client client, LocalDate date, Double amount, Boolean payed) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.amount = amount;
        this.payed = payed;
    }

    @Override
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    @Override
    public String toString() {
        return "Caddy{" +
                "id=" + id +
                ", client=" + (client != null ? client.toString() : "null") +
                ", date=" + (date != null ? date.toString() : "null") +
                ", amount=" + amount +
                ", payed=" + payed +
                '}';
    }
}
