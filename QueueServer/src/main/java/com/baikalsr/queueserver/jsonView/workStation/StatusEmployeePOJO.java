package com.baikalsr.queueserver.jsonView.workStation;

import com.baikalsr.queueserver.entity.Status;

public class StatusEmployeePOJO {
    private String name;
    private Status value;

    public StatusEmployeePOJO() {
    }

    public StatusEmployeePOJO(Status status) {
        name = Status.getRussianName(status);
        value = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getValue() {
        return value;
    }

    public void setValue(Status statusManager) {
        this.value = statusManager;
    }
}
