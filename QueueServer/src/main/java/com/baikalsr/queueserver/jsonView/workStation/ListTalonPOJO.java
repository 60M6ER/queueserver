package com.baikalsr.queueserver.jsonView.workStation;

import java.util.List;

public class ListTalonPOJO {
    private List<TalonInfoPOJO> tickets;

    public List<TalonInfoPOJO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TalonInfoPOJO> tickets) {
        this.tickets = tickets;
    }
}
