package com.baikalsr.queueserver.jsonView.workStation;

import java.util.UUID;

public class TicketStatusPOJO {
    private boolean ticket;
    private UUID id;

    public boolean isTicket() {
        return ticket;
    }

    public void setTicket(boolean ticket) {
        this.ticket = ticket;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
