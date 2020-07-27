package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.UI.WorkStationUI;
import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WorkStationUIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkStationUIService.class);

    @Autowired
    private WorkStationManager workStationManager;
    @Autowired
    private StatusManager statusManager;
    @Autowired
    private SecurityService securityService;



    public WorkStationUI getWorkStationUI() {
        Manager manager = securityService.getCurrentManager();

        WorkStationUI workStationUI = new WorkStationUI();
        workStationUI.setWorking(workStationManager.managerIsStartSession(manager));
        workStationUI.setCasement(statusManager.getCasement(manager));

        workStationUI.setTicketServicing(workStationManager.isTicketServicing(manager));
        workStationUI.setWaiting(workStationManager.isWaiting(manager));
        workStationUI.setServicing(workStationManager.isServicing(manager));
        workStationUI.setTicket(workStationManager.getServicingTicket(manager));
        workStationUI.setTypeDistribution(workStationManager.getTypeDistribution(workStationUI.getTicket()));

        return workStationUI;
    }

    public String startSession(WorkStationUI workStationUI) {
        String error = "";
        Manager manager = securityService.getCurrentManager();
        if (workStationUI.getCasement() < 1)
            error = "Введите правильный номер своего окна!";
        else
            try {
                workStationManager.startSession(workStationUI.getCasement(), manager);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
                error = "Возникла ошибка.";
            }


        if (error != "")
            LOGGER.warn("Пользователь: " + manager.getName() +
                    " {" +
                    error +
                    "}");
        return error;
    }

    public String endSession() {
        String error = "";
        Manager manager = securityService.getCurrentManager();
        Status status = statusManager.getStatusManager(manager);
        if (status == Status.WORKING_TIME || status == Status.INDIVIDUAL_TIME){
            try {
                workStationManager.endSession(manager);
            } catch(Exception e){
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                    error = "Возникла ошибка.";
                }
            }else if (status == Status.NOT_WORKING_TIME)
                error = "Вы уже завершили сессию";
        else {
            error = "Нельзя завершать сессию во время обслуживания!";
        }
        if (error != "")
            LOGGER.warn("Пользователь: " + manager.getName() +
                    " {" +
                    error +
                    "}");
        return error;
    }

    public String startServicing(WorkStationUI workStationUI) {
        String error = "";
        Manager manager = securityService.getCurrentManager();
        try {
            workStationManager.startServiceClient(manager);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            error = "Возникла ошибка.";
        }
        if (error != "")
            LOGGER.warn("Пользователь: " + manager.getName() +
                    " {" +
                    error +
                    "}");
        return error;
    }

    public String endServicing(WorkStationUI workStationUI) {
        String error = "";
        Manager manager = securityService.getCurrentManager();
        try {
            workStationManager.endServiceClient(manager);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            error = "Возникла ошибка.";
        }
        if (error != "")
            LOGGER.warn("Пользователь: " + manager.getName() +
                    " {" +
                    error +
                    "}");
        return error;
    }

}
