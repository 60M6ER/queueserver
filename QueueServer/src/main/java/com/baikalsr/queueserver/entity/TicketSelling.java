package com.baikalsr.queueserver.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class TicketSelling {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String sellingNumber;

    @ManyToOne
    @JoinColumn(name = "ticket")
    private Ticket ticket;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
