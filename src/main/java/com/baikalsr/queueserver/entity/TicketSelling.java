package com.baikalsr.queueserver.entity;

import javax.persistence.*;

@Entity
public class TicketSelling {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String sellingNumber;

    @ManyToOne
    @JoinColumn(name = "ticket")
    private Ticket ticket;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellingNumber() {
        return sellingNumber;
    }

    public void setSellingNumber(String sellingNumber) {
        this.sellingNumber = sellingNumber;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
