package com.baikalsr.queueserver.jsonView.workStation;

import com.baikalsr.queueserver.entity.Status;

import java.util.ArrayList;
import java.util.List;

public class StatusesEmployeePOJO {
    private StatusEmployeePOJO currentStatus;
    private List<StatusEmployeePOJO> statuses;
    private int casement;

    public StatusesEmployeePOJO() {
        statuses = new ArrayList<>();
    }

    public void addStatus(Status status) {
        statuses.add(new StatusEmployeePOJO(status));
    }

    public StatusEmployeePOJO getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(StatusEmployeePOJO currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = new StatusEmployeePOJO(currentStatus);
    }

    public List<StatusEmployeePOJO> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusEmployeePOJO> statuses) {
        this.statuses = statuses;
    }

    public int getCasement() {
        return casement;
    }

    public void setCasement(int casement) {
        this.casement = casement;
    }
}
