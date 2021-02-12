package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TypeDistribution;
import com.baikalsr.queueserver.jsonView.workStation.*;

import java.util.List;
import java.util.UUID;

public interface WorkStationManager {
    void startSession(int casement, Manager manager);
    void startSession(int casement, Manager manager, Status status);
    void endSession(Manager manager);
    void startServiceClient(Manager manager);
    void endServiceClient(Manager manager);
    boolean managerIsStartSession(Manager manager);
    boolean isTicketServicing(Manager manager);
    boolean isWaiting(Manager manager);
    boolean isServicing(Manager manager);
    Ticket getServicingTicket(Manager manager);
    TypeDistribution getTypeDistribution(Ticket ticket);
    CasementStatusPOJO getCasementStatus(Manager manager);
    StatusesEmployeePOJO getStatusesManager(Manager manager, Manager reqManager, boolean workStation);
    TalonInfoPOJO getTicket(UUID id);
    ListTalonPOJO getTicketsPaused(Manager manager);
    void setNewStatus(Manager manager, SetNewStatusPOJO setStatus) throws InterruptedException;
    void pausedTicket(Manager manager);
    void resumeServicingTicket(Manager manager, Ticket ticket);
    StatusNewTalonPOJO createTicketPartner(Manager manager);
    void setContractorTicket(Ticket ticket, SetContractorPOJO contractorPOJO);
    void addSellingTicket(Ticket ticket, AddSellingPojo sellingPojo);
}
