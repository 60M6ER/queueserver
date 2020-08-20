package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.UI.WorkStationUI;
import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.ManagerTicketRepo;
import com.baikalsr.queueserver.repository.ManagersStatusRepo;
import com.baikalsr.queueserver.repository.TicketRepo;
import com.baikalsr.queueserver.service.StatusManager;
import com.baikalsr.queueserver.service.WorkStationManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WorkStationManagerIMPL implements WorkStationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkStationManagerIMPL.class);


    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private ManagersStatusRepo managersStatusRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private ManagerTicketRepo managerTicketRepo;
    @Autowired
    private StatusManager statusManager;


    public WorkStationManagerIMPL() {
    }

    @Override
    public void startSession(int casement, Manager manager) {
        ManagersStatus managersStatus = new ManagersStatus();
        managersStatus.setCasement(casement)
                .setManager(manager)
                .setDate(new Date())
                .setStatus(Status.WORKING_TIME);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Начата рабочая сессия пользователя: " + manager.getName());
    }

    @Override
    public void endSession(Manager manager) {
        ManagersStatus managersStatus = new ManagersStatus();
        managersStatus.setManager(manager)
                .setDate(new Date())
                .setStatus(Status.NOT_WORKING_TIME);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Завершена рабочая сессия пользователя: " + manager.getName());
    }

    @Override
    public void startServiceClient(Manager manager) {
        Date date = new Date();
        ManagersStatus managersStatus = new ManagersStatus();

        managersStatus.setCasement(statusManager.getCasement(manager))
                .setStatus(Status.SERVICING_CLIENT)
                .setDate(date)
                .setManager(manager);

        Ticket ticket = getServicingTicket(manager);
        if (ticket == null){
            throw new NullPointerException("Попытка редактировать пустой талон! " + manager.getName());
        }
        ticket.setDateStartService(date);
        ticket.setStatus(TicketStatus.SERVICING);

        ticketRepo.save(ticket);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Начато обслуживание клиента с талоном: " + ticket.getName()
                + " в окне №" + managersStatus.getCasement()
                + " у менеджера: "+ manager.getName());
    }

    @Override
    public boolean managerIsStartSession(Manager manager) {
        return statusManager.managerIsStartSession(manager);
    }

    @Override
    public void endServiceClient(Manager manager) {
        Date date = new Date();
        ManagersStatus managersStatus = new ManagersStatus();

        managersStatus.setCasement(statusManager.getCasement(manager))
                .setStatus(Status.WORKING_TIME)
                .setDate(date)
                .setManager(manager);

        Ticket ticket = getServicingTicket(manager);
        if (ticket == null){
            throw new NullPointerException("Попытка редактировать пустой талон! " + manager.getName());
        }
        ticket.setDateEndService(date);
        ticket.setStatus(TicketStatus.ENDED);

        ticketRepo.save(ticket);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Завершено обслуживание клиента с талоном: " + ticket.getName()
                + " в окне №" + managersStatus.getCasement()
                + " у менеджера: "+ manager.getName());
    }

    @Override
    public boolean isTicketServicing(Manager manager) {
        Status status = statusManager.getStatusManager(manager);
        return status == Status.WAIT_CLIENT
                || status == Status.SERVICING_CLIENT
                || status == Status.SERVICING_REGULAR_CLIENT
                || status == Status.RECEPTION_EXPEDITION;
    }

    @Override
    public boolean isWaiting(Manager manager) {
        return statusManager.getStatusManager(manager) == Status.WAIT_CLIENT;
    }

    @Override
    public boolean isServicing(Manager manager) {
        Status status = statusManager.getStatusManager(manager);
        return status == Status.SERVICING_CLIENT
                || status == Status.SERVICING_REGULAR_CLIENT
                || status == Status.RECEPTION_EXPEDITION;
    }

    @Override
    public Ticket getServicingTicket(Manager manager) {
        Ticket ticket = ticketRepo.getServicingTicketByManger(manager.getId());

        return ticket;
    }

    @Override
    public TypeDistribution getTypeDistribution(Ticket ticket) {
        if (ticket != null)
            return managerTicketRepo.getLastByTicketId(ticket.getId()).getTypeDistribution();
        return null;
    }
}
