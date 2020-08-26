package com.baikalsr.queueserver.entity;

import java.util.Date;
import java.util.UUID;

public class PrintJob {
    private UUID number;
    private String name;
    private byte[] content;
    private StatusJob statusJob;
    private Date startPrint;

    public PrintJob() {
        content = new byte[0];
    }

    public PrintJob addContent(byte[] bytes) {
        addToContent(bytes);
        return this;
    }

    public PrintJob addContent(byte oneByte) {
        addToContent(new byte[]{oneByte});
        return this;
    }

    private void addToContent(byte[] bytes) {
        //content.add(bytes);
        byte[] newContent = new byte[content.length + bytes.length];
        for (int i = 0; i < content.length; i++)
            newContent[i] = content[i];
        for (int i = content.length; i < newContent.length; i++)
            newContent[i] = bytes[i - content.length];
        content = newContent;
    }

    public PrintJob(String name) {
        this.name = name;
    }

    public UUID getNumber() {
        return number;
    }

    public void setNumber(UUID number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public StatusJob getStatusJob() {
        return statusJob;
    }

    public void setStatusJob(StatusJob statusJob) {
        this.statusJob = statusJob;
    }

    public Date getStartPrint() {
        return startPrint;
    }

    public void setStartPrint(Date startPrint) {
        this.startPrint = startPrint;
    }
}
