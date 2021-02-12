package com.baikalsr.queueserver.service.reports;

import javax.persistence.EntityManager;

public class ReportBuilder extends Thread{
    protected EntityManager entityManager;
    protected String status;
    protected String message;

    public ReportBuilder(String name, EntityManager entityManager) {
        super(name);
        this.entityManager = entityManager;
    }

    public String getStatusRep() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return message;
    }
}
