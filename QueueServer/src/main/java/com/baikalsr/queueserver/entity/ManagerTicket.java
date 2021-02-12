package com.baikalsr.queueserver.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class ManagerTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Date date;

    @Enumerated(EnumType.STRING)
    private TypeDistribution typeDistribution;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TypeDistribution getTypeDistribution() {
        return typeDistribution;
    }

    public void setTypeDistribution(TypeDistribution typeDistribution) {
        this.typeDistribution = typeDistribution;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
