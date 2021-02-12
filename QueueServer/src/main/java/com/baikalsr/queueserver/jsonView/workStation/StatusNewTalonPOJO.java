package com.baikalsr.queueserver.jsonView.workStation;

import java.util.UUID;

public class StatusNewTalonPOJO {
    private String status;
    private UUID idTicket;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(UUID idTicket) {
        this.idTicket = idTicket;
    }
}
