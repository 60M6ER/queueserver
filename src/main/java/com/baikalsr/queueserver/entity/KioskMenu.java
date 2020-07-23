package com.baikalsr.queueserver.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class KioskMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String menuJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    @Override
    public String toString() {
        return name;
    }
    public void setMenuJson(String menuJson) {
        this.menuJson = menuJson;
    }
}
