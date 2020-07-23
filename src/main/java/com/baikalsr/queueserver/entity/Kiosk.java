package com.baikalsr.queueserver.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Kiosk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String IP;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getIP() {
        return IP;
    }
}
