package com.baikalsr.queueserver.jsonView;

import java.util.ArrayList;
import java.util.List;

public class ServiceList {
    private String type;
    private Long idMenu;
    private String message;
    private List<MenuUnitService> list;



    public ServiceList() {
        list = new ArrayList<>();
    }

    public ServiceList(String message) {
        list = new ArrayList<>();
        type = "Error";
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MenuUnitService> getList() {
        return list;
    }

    public void setList(List<MenuUnitService> list) {
        this.list = list;
    }

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
