package com.baikalsr.queueserver.jsonView.workStation;

import com.baikalsr.queueserver.entity.Contractor;

import java.util.UUID;

public class SetContractorPOJO {
    private UUID idTicket;
    private Contractor contractor;

    public UUID getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(UUID idTicket) {
        this.idTicket = idTicket;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }
}
