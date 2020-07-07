package com.baikalsr.queueserver.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class KioskMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    private String menuJson;
}
