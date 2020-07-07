package com.baikalsr.queueserver.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int window;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;
}
