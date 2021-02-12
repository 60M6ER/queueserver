package com.baikalsr.queueserver.jsonView.administration;

import com.baikalsr.queueserver.jsonView.workStation.StatusesEmployeePOJO;

import java.util.ArrayList;
import java.util.List;

public class ManagerDataPOJO {
    private long managerID;
    private String nameManager;
    private StatusesEmployeePOJO statuses;
    private QueuesPOJO queues;
    private List<SelectedServicePOJO> services;

    public ManagerDataPOJO() {
        services = new ArrayList<>();
    }

    public void addService(SelectedServicePOJO selectedServicePOJO) {
        services.add(selectedServicePOJO);
    }

    public long getManagerID() {
        return managerID;
    }

    public void setManagerID(long managerID) {
        this.managerID = managerID;
    }

    public String getNameManager() {
        return nameManager;
    }

    public void setNameManager(String nameManager) {
        this.nameManager = nameManager;
    }

    public StatusesEmployeePOJO getStatuses() {
        return statuses;
    }

    public void setStatuses(StatusesEmployeePOJO statuses) {
        this.statuses = statuses;
    }

    public QueuesPOJO getQueues() {
        return queues;
    }

    public void setQueues(QueuesPOJO queues) {
        this.queues = queues;
    }

    public List<SelectedServicePOJO> getServices() {
        return services;
    }

    public void setServices(List<SelectedServicePOJO> services) {
        this.services = services;
    }
}
