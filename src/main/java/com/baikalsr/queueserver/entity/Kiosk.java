package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.editorImpl.KioskEdit;
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

    private String comment;

    private boolean active;

    public Kiosk() {
    }

    public Kiosk(KioskEdit kioskEdit) {
        id = kioskEdit.getId();
        name = kioskEdit.getName();
        IP = kioskEdit.getIP();
        queue = kioskEdit.getQueue();
        comment = kioskEdit.getComment();
        active = kioskEdit.isActive();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
