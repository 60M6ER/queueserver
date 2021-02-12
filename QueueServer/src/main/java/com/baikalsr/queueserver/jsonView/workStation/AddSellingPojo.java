package com.baikalsr.queueserver.jsonView.workStation;

import java.util.UUID;

public class AddSellingPojo {
    private UUID idTicket;
    private String numberSelling;

    public UUID getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(UUID idTicket) {
        this.idTicket = idTicket;
    }

    public String getNumberSelling() {
        return numberSelling;
    }

    public void setNumberSelling(String numberSelling) {
        this.numberSelling = numberSelling;
    }
}
