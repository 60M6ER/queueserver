package com.baikalsr.queueserver.UI;

import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TypeDistribution;

public class WorkStationUI {
    private boolean working;
    private int casement;
    private String errorEditSession = "";
    private boolean ticketServicing;
    private TypeDistribution typeDistribution;
    private boolean waiting;
    private boolean servicing;
    private Ticket ticket;
    private Long ticketID;



    public WorkStationUI() {
    }


    public TypeDistribution getTypeDistribution() {
        return typeDistribution;
    }

    public String getTypeDistributionToString() {
        if (typeDistribution == null)
            return "NaN";

        if (this.typeDistribution == TypeDistribution.AUTO)
            return "назначен";
        else
            return "перенаправлен";
    }

    public String ticketToString() {
        if (ticket == null)
            return "NaN";
        return ticket.getName();
    }

    public void setTypeDistribution(TypeDistribution typeDistribution) {
        this.typeDistribution = typeDistribution;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public boolean isServicing() {
        return servicing;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public int getCasement() {
        return casement;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public boolean isTicketServicing() {
        return ticketServicing;
    }

    public void setTicketServicing(boolean ticketServicing) {
        this.ticketServicing = ticketServicing;
    }

    public String getErrorEditSession() {
        return errorEditSession;
    }

    public Long getTicketID() {
        return ticketID;
    }

    public void setTicketID(Long ticketID) {
        this.ticketID = ticketID;
    }

    public void setErrorEditSession(String errorEditSession) {
        this.errorEditSession = errorEditSession;
    }

    public void setCasement(int casement) {
        this.casement = casement;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public void setServicing(boolean servicing) {
        this.servicing = servicing;
    }
}
