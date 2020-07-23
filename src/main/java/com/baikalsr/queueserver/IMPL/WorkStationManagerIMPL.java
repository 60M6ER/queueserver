package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.UI.WorkStationUI;
import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.ManagerTicketRepo;
import com.baikalsr.queueserver.repository.ManagersStatusRepo;
import com.baikalsr.queueserver.repository.TicketRepo;
import com.baikalsr.queueserver.service.WorkStationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class WorkStationManagerIMPL implements WorkStationManager {
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private ManagersStatusRepo managersStatusRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private ManagerTicketRepo managerTicketRepo;

    private Map<String, Object> poolWorkStations;

    public WorkStationManagerIMPL() {
        poolWorkStations = new HashMap<>();
    }

    @Override
    public WorkStationUI getWorkStationUI(String key) {
        if (!poolWorkStations.containsKey(key))
            poolWorkStations.put(key, createWorkStationUI(key));

        getDistribTalon((WorkStationUI) poolWorkStations.get(key));
        return (WorkStationUI) poolWorkStations.get(key);
    }

    @Override
    public WorkStationUI updateWorkStationUI(WorkStationUI workStationUI) {
        WorkStationUI workStationUIPool = getWorkStationUI(workStationUI.getLoginAD());
        //workStationUIPool.setWorking(workStationUI.isWorking());
        workStationUIPool.setCasement(workStationUI.getCasement());
        //workStationUIPool.setStatus(workStationUI.getStatus());
        return workStationUIPool;
    }

    @Override
    public void startSession(int casement, String login) {
        Manager manager = managerRepo.findByLoginAD(login);
        ManagersStatus managersStatus = new ManagersStatus();
        managersStatus.setCasement(casement)
                .setManager(manager)
                .setDate(new Date())
                .setStatus(Status.WORKING_TIME);
        managersStatusRepo.save(managersStatus);
    }

    @Override
    public void endSession(String login) {
        Manager manager = managerRepo.findByLoginAD(login);
        ManagersStatus managersStatus = new ManagersStatus();
        managersStatus.setManager(manager)
                .setDate(new Date())
                .setStatus(Status.NOT_WORKING_TIME);
        managersStatusRepo.save(managersStatus);
    }

    @Override
    public String getTypeDistributionToString() {
        TypeDistribution typeDistribution
        public String getTypeDistributionToString() {
            if (typeDistribution == TypeDistribution.AUTO)
                return "назначен";
            else
                return "перенаправлен";
        }
    }

    private WorkStationUI createWorkStationUI(String key) {
        Manager manager = managerRepo.findByLoginAD(key);
        ManagersStatus managersStatus = managersStatusRepo.getLastByManagerId(manager.getId());
        WorkStationUI workStationUI = new WorkStationUI(key);

        if (managersStatus != null) {
            workStationUI.setStatus(managersStatus);
            workStationUI.setCasement(managersStatus.getCasement());
            workStationUI.setWorking(managersStatus.getStatus() == Status.NOT_WORKING_TIME ? false : true);
        }
        return workStationUI;
    }

    @Override
    public void serviceClient(String login) {
        Date date = new Date();
        Manager manager = managerRepo.findByLoginAD(login);
        ManagersStatus managersStatus = new ManagersStatus();
        WorkStationUI workStationUI = getWorkStationUI(login);
        managersStatus.setCasement(workStationUI.getCasement())
                .setStatus(Status.SERVICING_CLIENT)
                .setDate(date)
                .setManager(manager);
        workStationUI.setStatus(managersStatus);

        Ticket ticket = workStationUI.getTicket();
        ticket.setDateStartService(date);
        ticket.setStatus(TicketStatus.SERVICING);

        ticketRepo.save(ticket);
        managersStatusRepo.save(managersStatus);
    }

    @Override
    public void endServiceClient(String login) {
        Date date = new Date();
        Manager manager = managerRepo.findByLoginAD(login);
        ManagersStatus managersStatus = new ManagersStatus();
        WorkStationUI workStationUI = getWorkStationUI(login);
        managersStatus.setCasement(workStationUI.getCasement())
                .setStatus(Status.WORKING_TIME)
                .setDate(date)
                .setManager(manager);
        workStationUI.setStatus(managersStatus);
        workStationUI.setTicketServicing(false);

        Ticket ticket = workStationUI.getTicket();
        ticket.setDateEndService(date);
        ticket.setStatus(TicketStatus.ENDED);

        ticketRepo.save(ticket);
        managersStatusRepo.save(managersStatus);
    }

    private void getDistribTalon(WorkStationUI workStationUI) {
        Manager manager = managerRepo.findByLoginAD(workStationUI.getLoginAD());
        ManagersStatus managersStatusDB = managersStatusRepo.getLastByManagerId(manager.getId());

        if (managersStatusDB != null) {
            if (managersStatusDB.getStatus() == Status.WAIT_CLIENT && !workStationUI.isTicketServicing()){
                workStationUI.setStatus(managersStatusDB);
                workStationUI.setTicket(ticketRepo.getTicketByManger(manager.getId()));
                workStationUI.setTicketServicing(true);
                workStationUI.setTypeDistribution(managerTicketRepo.getLastByTicketId(workStationUI.getTicket().getId()).getTypeDistribution());
            }
        }

    }

    public Map<String, Object> getPoolWorkStations() {
        return poolWorkStations;
    }

    public void setPoolWorkStations(Map<String, Object> poolWorkStations) {
        this.poolWorkStations = poolWorkStations;
    }
}
