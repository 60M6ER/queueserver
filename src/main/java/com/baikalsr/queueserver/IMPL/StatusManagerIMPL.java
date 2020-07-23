package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.ManagersStatus;
import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.ManagersStatusRepo;
import com.baikalsr.queueserver.service.StatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusManagerIMPL implements StatusManager {
    @Autowired
    private ManagersStatusRepo managersStatusRepo;
    @Autowired
    private ManagerRepo managerRepo;

    @Override
    public Status getStatusManager(Manager manager) {
        ManagersStatus managersStatus = managersStatusRepo.getLastByManagerId(manager.getId());

        return managersStatus == null ? Status.NOT_WORKING_TIME : managersStatus.getStatus();
    }

    @Override
    public Status getStatusManager(String login) {

        Manager manager = managerRepo.findByLoginAD(login);
        ManagersStatus managersStatus = managersStatusRepo.getLastByManagerId(manager.getId());

        return managersStatus == null ? Status.NOT_WORKING_TIME : managersStatus.getStatus();
    }

    @Override
    public String statusToString(Status status) {
        if (status == Status.INDIVIDUAL_TIME)
            return "Личное время";
        if (status == Status.NOT_WORKING_TIME)
            return "Нерабочее время";
        if (status == Status.WAIT_CLIENT)
            return "Ожидание клиента";
        if (status == Status.SERVICING_CLIENT)
            return "Обслуживание клиента";
        if (status == Status.SERVICING_REGULAR_CLIENT)
            return "Обслуживание постоянного клиента";
        if (status == Status.RECEPTION_EXPEDITION)
            return "Прием экспедиции";
        if (status == Status.WORKING_TIME)
            return "Рабочее время";
        return "Статус непоределен!";
    }
}
