package com.baikalsr.queueserver.entities;

import javax.persistence.*;

@Entity
public class Numerator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int currentQuantity;

    @OneToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;
}
