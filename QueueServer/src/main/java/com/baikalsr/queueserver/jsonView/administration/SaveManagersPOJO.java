package com.baikalsr.queueserver.jsonView.administration;

import com.baikalsr.queueserver.jsonView.workStation.StatusEmployeePOJO;

import java.util.List;

public class SaveManagersPOJO {
    private long managerID;
    private String currentStatus;
    private int casement;
    private long current_queue;
    private List<SelectedServicePOJO> services;

    public long getManagerID() {
        return managerID;
    }

    public void setManagerID(long managerID) {
        this.managerID = managerID;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getCasement() {
        return casement;
    }

    public void setCasement(int casement) {
        this.casement = casement;
    }

    public long getCurrent_queue() {
        return current_queue;
    }

    public void setCurrent_queue(long current_queue) {
        this.current_queue = current_queue;
    }

    public List<SelectedServicePOJO> getServices() {
        return services;
    }

    public void setServices(List<SelectedServicePOJO> services) {
        this.services = services;
    }
}
