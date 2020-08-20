package com.baikalsr.queueserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ManagerTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Date date;

    @Enumerated(EnumType.STRING)
    private TypeDistribution typeDistribution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
