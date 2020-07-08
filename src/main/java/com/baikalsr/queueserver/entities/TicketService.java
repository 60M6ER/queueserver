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
}
