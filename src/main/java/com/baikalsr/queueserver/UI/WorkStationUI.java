package com.baikalsr.queueserver.UI;

import com.baikalsr.queueserver.entity.ManagersStatus;
import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TypeDistribution;

public class WorkStationUI {
    private boolean working;
    private String loginAD;
    private ManagersStatus status;
    private int casement;
    private Ticket ticket;
    private boolean ticketServicing;
    private TypeDistribution typeDistribution;

    public WorkStationUI() {
    }

    public WorkStationUI(String key) {
        loginAD = key;
    }

    public TypeDistribution getTypeDistribution() {
        return typeDistribution;
    }

    public String getTypeDistributionToString() {
        if (this.typeDistribution == TypeDistribution.AUTO)
            return "назначен";
        else
            return "перенаправлен";
    }

    public void setTypeDistribution(TypeDistribution typeDistribution) {
        this.typeDistribution = typeDistribution;
    }

    public boolean isWaiting() {
        if (this.status.getStatus() == Status.WAIT_CLIENT)
            return true;
        return false;
    }

    public boolean isServicing() {
        if (this.status.getStatus() == Status.SERVICING_CLIENT
        || this.status.getStatus() == Status.SERVICING_REGULAR_CLIENT)
            return true;
        return false;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public String getLoginAD() {
        return loginAD;
    }

    public ManagersStatus getStatus() {
        return status;
    }

    public void setStatus(ManagersStatus status) {
        this.status = status;
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

    public void setCasement(int casement) {
        this.casement = casement;
    }

    public void setLoginAD(String loginAD) {
        this.loginAD = loginAD;
    }
}
