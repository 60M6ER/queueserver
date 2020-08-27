package com.baikalsr.queueserver.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String nameClient;
    private UUID printJobID;
    private Date dateCreate;
    private Date datePrinted;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public UUID getPrintJobID() {
        return printJobID;
    }

    public void setPrintJobID(UUID printJobID) {
        this.printJobID = printJobID;
    }

    public Date getDatePrinted() {
        return datePrinted;
    }

    public void setDatePrinted(Date datePrinted) {
        this.datePrinted = datePrinted;
    }

    public List<TicketSelling> getTicketSellings() {
        return ticketSellings;
    }
    @Override
    public String toString() {
        return name;
    }
    public void setTicketSellings(List<TicketSelling> ticketSellings) {
        this.ticketSellings = ticketSellings;
    }
}
