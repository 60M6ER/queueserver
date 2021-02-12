package com.baikalsr.queueserver.jsonView.workStation;

public class ElQueuePOJO {
    private String name;
    private int clientsInQueue;


    public int getClientsInQueue() {
        return clientsInQueue;
    }

    public void setClientsInQueue(int clientsInQueue) {
        this.clientsInQueue = clientsInQueue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
