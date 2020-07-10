package com.baikalsr.queueserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ManagersStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int casement;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCasement() {
        return casement;
    }

    public void setCasement(int casement) {
        this.casement = casement;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
