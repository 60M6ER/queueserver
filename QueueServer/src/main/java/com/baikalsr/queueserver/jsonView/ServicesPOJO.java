package com.baikalsr.queueserver.jsonView;

import com.baikalsr.queueserver.entity.TicketService;

public class ServicesPOJO {
    private String id;
    private String name;

    public ServicesPOJO() {
    }

    public ServicesPOJO(TicketService service) {
        this.id = service.getId().toString();
        this.name = service.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
