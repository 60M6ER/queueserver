package com.baikalsr.queueserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ManagersStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int casement;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Long getId() {
        return id;
    }

    public ManagersStatus setId(Long id) {
        this.id = id;
        return this;
    }

    public int getCasement() {
        return casement;
    }

    public ManagersStatus setCasement(int casement) {
        this.casement = casement;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public ManagersStatus setDate(Date date) {
        this.date = date;
        return this;
    }

    public Manager getManager() {
        return manager;
    }

    public ManagersStatus setManager(Manager manager) {
        this.manager = manager;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public ManagersStatus setStatus(Status status) {
        this.status = status;
        return this;
    }
}
