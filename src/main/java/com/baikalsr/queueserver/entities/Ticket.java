package com.baikalsr.queueserver.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    private String nameClient;
    private Date dateCreate;
    private Date dateDistrib;
    private Date dateStartService;
    private Date dateEndService;
    private String comment;

    @Enumerated(EnumType.ORDINAL)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "ticketService_id")
    private TicketService service;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    @ManyToOne
    @JoinColumn(name = "reason_id")
    private Reason reason;

    @OneToMany(mappedBy = "ticket_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketSelling> ticketSellings;
}
