package com.baikalsr.queueserver.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    private int quantityTablo;

    private int quantityInform;

    private int timeInform;

    @ManyToOne
    @JoinColumn(name = "kioskMenu_id")
    private KioskMenu kioskMenu;

    private Set<User> users;

    @OneToOne(mappedBy = "queue_id")
    private Numerator numerator;

    @OneToMany(mappedBy = "queue_id", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantityTablo(int quantityTablo) {
        this.quantityTablo = quantityTablo;
    }

    public void setQuantityInform(int quantityInform) {
        this.quantityInform = quantityInform;
    }

    public void setTimeInform(int timeInform) {
        this.timeInform = timeInform;
    }

    public void setKioskMenu(KioskMenu kioskMenu) {
        this.kioskMenu = kioskMenu;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantityTablo() {
        return quantityTablo;
    }

    public int getQuantityInform() {
        return quantityInform;
    }

    public int getTimeInform() {
        return timeInform;
    }

    public KioskMenu getKioskMenu() {
        return kioskMenu;
    }
}
