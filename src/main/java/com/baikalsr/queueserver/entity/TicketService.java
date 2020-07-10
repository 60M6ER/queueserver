package com.baikalsr.queueserver.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class TicketService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    private int priority;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private List<Manager> managers;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }
}
