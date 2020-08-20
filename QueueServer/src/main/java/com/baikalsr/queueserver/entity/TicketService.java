package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.editorImpl.TicketServiceEdit;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class TicketService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private int priority;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private List<Manager> managers;


    public TicketService() {
    }

    public TicketService(TicketServiceEdit serviceEdit) {
        id = serviceEdit.getId();
        name = serviceEdit.getName();
        priority = serviceEdit.getPriority();
        managers = serviceEdit.getManagers();
    }

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == String.class)
            return obj.equals("" + id);
        return id == ((TicketService) obj).id;
    }
    @Override
    public String toString() {
        return name;
    }
    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }
}
