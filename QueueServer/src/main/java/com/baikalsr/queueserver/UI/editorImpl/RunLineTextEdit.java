package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.RunLineText;
import com.baikalsr.queueserver.entity.Tablo;
import com.baikalsr.queueserver.repository.QueueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class RunLineTextEdit {
    private String UUID;
    private UUID id;

    private String name;
    private String text;
    private boolean active;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private boolean nonDate;

    public RunLineTextEdit() {
        UUID = java.util.UUID.randomUUID().toString();
    }


    public RunLineTextEdit cloneObj(){
        RunLineTextEdit runLineTextEdit = new RunLineTextEdit();

        return runLineTextEdit;
    }

    public RunLineTextEdit(RunLineText runLineText) {
        UUID = java.util.UUID.randomUUID().toString();
        id = runLineText.getId();
        name = runLineText.getName();
        text = runLineText.getText();
        active = runLineText.isActive();
        startDate = runLineText.getStartDate();
        endDate = runLineText.getEndDate();
        nonDate = runLineText.isNonDate();
    }

    public void rebuildRunLineTextEdit(RunLineText runLineText) {
        id = runLineText.getId();
        name = runLineText.getName();
        text = runLineText.getText();
        active = runLineText.isActive();
        startDate = runLineText.getStartDate();
        endDate = runLineText.getEndDate();
        nonDate = runLineText.isNonDate();
    }

    public void updateRunLineTextEdit(RunLineTextEdit runLineTextEdit) {

    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public java.util.UUID getId() {
        return id;
    }

    public void setId(java.util.UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isNonDate() {
        return nonDate;
    }

    public void setNonDate(boolean nonDate) {
        this.nonDate = nonDate;
    }
}
