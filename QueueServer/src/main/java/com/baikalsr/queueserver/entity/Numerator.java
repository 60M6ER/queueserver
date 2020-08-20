package com.baikalsr.queueserver.entity;

import javax.persistence.*;

@Entity
public class Numerator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int currentQuantity;

    @OneToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
