package com.baikalsr.queueserver.entity;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuJson() {
        return menuJson;
    }

    public void setMenuJson(String menuJson) {
        this.menuJson = menuJson;
    }
}
