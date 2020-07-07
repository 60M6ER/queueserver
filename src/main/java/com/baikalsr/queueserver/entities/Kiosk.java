package com.baikalsr.queueserver.entities;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Mod10Check;

import javax.persistence.*;

@Entity
public class Kiosk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    private String IP;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Queue getQueue() {
        return queue;
    }
}
