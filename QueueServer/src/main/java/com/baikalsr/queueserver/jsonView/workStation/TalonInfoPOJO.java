package com.baikalsr.queueserver.jsonView.workStation;

import com.baikalsr.queueserver.entity.Contractor;
import com.baikalsr.queueserver.entity.TicketSelling;
import com.baikalsr.queueserver.entity.TicketStatus;

import java.util.List;
import java.util.UUID;

public class TalonInfoPOJO {
    private String id;
    private String name;
    private TicketStatus status;
    private String dateStartService;
    private Long timeOnPause;
    private String service;
    private Contractor contractor;
    private List<TicketSelling> sellings;

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

    public String getDateStartService() {
        return dateStartService;
    }

    public void setDateStartService(String dateStartService) {
        this.dateStartService = dateStartService;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public List<TicketSelling> getSellings() {
        return sellings;
    }

    public void setSellings(List<TicketSelling> sellings) {
        this.sellings = sellings;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Long getTimeOnPause() {
        return timeOnPause;
    }

    public void setTimeOnPause(Long timeOnPause) {
        this.timeOnPause = timeOnPause;
    }
}
