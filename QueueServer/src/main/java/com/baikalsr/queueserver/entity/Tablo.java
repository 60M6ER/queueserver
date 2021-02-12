package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.editorImpl.KioskEdit;
import com.baikalsr.queueserver.UI.editorImpl.TabloEdit;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Tablo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String IP;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    private String comment;

    private int countLinesOnPage;

    private int landscape;
    private boolean update;
    private boolean active;
    private boolean test;
    private int versionTabloPage;
    private boolean runLinesOn;

    @Enumerated(EnumType.STRING)
    private StatusDevice statusDevice;
    private String errorMessage;

    public Tablo() {
    }

    public Tablo(TabloEdit tabloEdit) {
        id = tabloEdit.getId();
        name = tabloEdit.getName();
        IP = tabloEdit.getIP();
        queue = tabloEdit.getQueue();
        comment = tabloEdit.getComment();
        active = tabloEdit.isActive();
        countLinesOnPage = tabloEdit.getCountLinesOnPage();
        landscape = tabloEdit.getLandscape();
        update = tabloEdit.isUpdate();
        test = tabloEdit.isTest();
        versionTabloPage = tabloEdit.getVersionTabloPage();
        runLinesOn = tabloEdit.isRunLinesOn();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public StatusDevice getStatusDevice() {
        return statusDevice;
    }

    public void setStatusDevice(StatusDevice statusDevice) {
        this.statusDevice = statusDevice;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getIP() {
        return IP;
    }

    public int getCountLinesOnPage() {
        return countLinesOnPage;
    }

    public void setCountLinesOnPage(int countLinesOnPage) {
        this.countLinesOnPage = countLinesOnPage;
    }

    public int getLandscape() {
        return landscape;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getVersionTabloPage() {
        return versionTabloPage;
    }

    public void setVersionTabloPage(int versionTabloPage) {
        this.versionTabloPage = versionTabloPage;
    }

    public void setVersionTabloPage(Integer versionTabloPage) {
        if (versionTabloPage == null)
            this.versionTabloPage = 0;
        this.versionTabloPage = versionTabloPage;
    }

    public void setLandscape(int landscape) {
        this.landscape = landscape;
    }

    public boolean isRunLinesOn() {
        return runLinesOn;
    }

    public void setRunLinesOn(boolean runLinesOn) {
        this.runLinesOn = runLinesOn;
    }
}
