package com.baikalsr.queueserver.entities;

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
}
