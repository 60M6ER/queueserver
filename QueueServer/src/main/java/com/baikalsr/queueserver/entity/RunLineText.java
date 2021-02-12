package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.editorImpl.RunLineTextEdit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class RunLineText {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String text;
    private boolean active;
    private boolean nonDate;
    private Date startDate;
    private Date endDate;

    public RunLineText() {
    }

    public RunLineText(RunLineTextEdit runLineTextEdit) {
        id = runLineTextEdit.getId();
        name = runLineTextEdit.getName();
        text = runLineTextEdit.getText();
        active = runLineTextEdit.isActive();
        nonDate = runLineTextEdit.isNonDate();
        startDate = runLineTextEdit.getStartDate();
        endDate = runLineTextEdit.getEndDate();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    @Override
    public String toString() {
        return name;
    }
}
