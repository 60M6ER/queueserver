package com.baikalsr.queueserver.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class TicketStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Date date;
    private Long dateLong;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getDateLong() {
        return dateLong;
    }

    public void setDateLong(Long dateLong) {
        this.dateLong = dateLong;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
