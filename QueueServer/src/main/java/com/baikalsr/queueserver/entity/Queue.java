package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.editorImpl.QueueEdit;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private int quantityTablo;

    private int quantityInform;

    private int timeInform;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "kioskMenu_id")
    private KioskMenu kioskMenu;

    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manager> managers;

    @OneToOne(mappedBy = "queue")
    private Numerator numerator;

    public Queue() {
    }

    public Queue(QueueEdit queueEdit) {
        id = queueEdit.getId();
        name = queueEdit.getName();
        quantityTablo = queueEdit.getQuantityTablo();
        quantityInform = queueEdit.getQuantityInform();
        timeInform = queueEdit.getTimeInform();
        kioskMenu = queueEdit.getKioskMenu();
        managers = queueEdit.getManagers();
        numerator = queueEdit.getNumerator();
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }

    public void setId(Long id) {
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

    public Long getId() {
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

    public Numerator getNumerator() {
        return numerator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return this.id == ((Queue) obj).id;
        }catch (Exception e){
            return false;
        }

    }
    @Override
    public String toString() {
        return name;
    }
    public void setNumerator(Numerator numerator) {
        this.numerator = numerator;
    }
}
