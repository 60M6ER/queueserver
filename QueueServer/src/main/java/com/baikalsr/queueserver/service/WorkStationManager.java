package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TypeDistribution;

public interface WorkStationManager {
    void startSession(int casement, Manager manager);
    void endSession(Manager manager);
    void startServiceClient(Manager manager);
    void endServiceClient(Manager manager);
    boolean managerIsStartSession(Manager manager);
    boolean isTicketServicing(Manager manager);
    boolean isWaiting(Manager manager);
    boolean isServicing(Manager manager);
    Ticket getServicingTicket(Manager manager);
    TypeDistribution getTypeDistribution(Ticket ticket);
}
