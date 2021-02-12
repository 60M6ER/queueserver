package com.baikalsr.queueserver.jsonView.workStation;

import java.util.UUID;

public class PauseTicketPojo {
    private String idUser;
    private UUID idTicket;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public UUID getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(UUID idTicket) {
        this.idTicket = idTicket;
    }
}
