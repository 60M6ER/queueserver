package com.baikalsr.printservice.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PrintJob {
    static private int count = 0;

    private UUID number;
    private String name;
    private StatusJob statusJob;
    private byte[] content;
    private Date startPrint;

    public PrintJob(PrintJob printJob){
        this.number = UUID.randomUUID();
        this.name = printJob.getName();
        this.statusJob = StatusJob.WAIT;
        this.content = printJob.getContent();
        this.startPrint = new Date();
    }

    public PrintJob(){
        number = UUID.randomUUID();
    }

    public UUID getNumber() {
        return number;
    }

    public void setNumber(UUID number) {
        this.number = number;
    }

    public StatusJob getStatusJob() {
        return statusJob;
    }

    public void setStatusJob(StatusJob statusJob) {
        this.statusJob = statusJob;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartPrint() {
        return startPrint;
    }

    public void setStartPrint(Date startPrint) {
        this.startPrint = startPrint;
    }
}
