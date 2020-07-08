package com.baikalsr.queueserver.entities;

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
}
