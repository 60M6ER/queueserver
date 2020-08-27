package com.baikalsr.queueserver.jsonView;

import com.baikalsr.queueserver.entity.KioskMenu;

public class MenuUnitService {
    private String name;
    private Long id;

    public MenuUnitService() {
    }

    public MenuUnitService(KioskMenu kioskMenu) {
        this.name = kioskMenu.getName();
        this.id = kioskMenu.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
