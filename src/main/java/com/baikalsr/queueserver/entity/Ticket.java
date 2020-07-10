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

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketSelling> ticketSellings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateDistrib() {
        return dateDistrib;
    }

    public void setDateDistrib(Date dateDistrib) {
        this.dateDistrib = dateDistrib;
    }

    public Date getDateStartService() {
        return dateStartService;
    }

    public void setDateStartService(Date dateStartService) {
        this.dateStartService = dateStartService;
    }

    public Date getDateEndService() {
        return dateEndService;
    }

    public void setDateEndService(Date dateEndService) {
        this.dateEndService = dateEndService;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketService getService() {
        return service;
    }

    public void setService(TicketService service) {
        this.service = service;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public List<TicketSelling> getTicketSellings() {
        return ticketSellings;
    }

    public void setTicketSellings(List<TicketSelling> ticketSellings) {
        this.ticketSellings = ticketSellings;
    }
}
