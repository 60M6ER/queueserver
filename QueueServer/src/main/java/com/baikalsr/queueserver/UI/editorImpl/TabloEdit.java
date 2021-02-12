package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.Kiosk;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.Tablo;
import com.baikalsr.queueserver.repository.QueueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class TabloEdit {

    @Autowired QueueRepo queueRepo;

    private String UUID;
    private Long id;
    private String name;
    private String IP;
    private Queue queue;
    private String comment;
    private int countLinesOnPage;
    private int landscape;
    private boolean active;
    private boolean update;
    private boolean test;
    private int versionTabloPage;
    private boolean runLinesOn;

    public ArrayList<Queue> getSelectQueues() {
        ArrayList<Queue> selectQueues = new ArrayList<>();
        Queue queueNull = new Queue();
        queueNull.setId(-1l);
        queueNull.setName("--");
        //selectQueues.add(queueNull);

        List<Queue> allQueues = queueRepo.findAll();

        for (Queue queueAll : allQueues) {
            selectQueues.add(queueAll);
        }
        return selectQueues;
    }

    public TabloEdit() {
        UUID = java.util.UUID.randomUUID().toString();
    }


    public TabloEdit cloneObj(){
        TabloEdit kioskEdit = new TabloEdit();
        kioskEdit.setQueueRepo(this.queueRepo);
        return kioskEdit;
    }

    public TabloEdit(Tablo tablo) {
        UUID = java.util.UUID.randomUUID().toString();
        id = tablo.getId();
        name = tablo.getName();
        IP = tablo.getIP();
        queue = tablo.getQueue();
        comment = tablo.getComment();
        countLinesOnPage = tablo.getCountLinesOnPage();
        landscape = tablo.getLandscape();
        active = tablo.isActive();
        update = tablo.isUpdate();
        test = tablo.isTest();
        versionTabloPage = tablo.getVersionTabloPage();
        runLinesOn = tablo.isRunLinesOn();
    }

    public void rebuildTabloEdit(Tablo tablo) {
        id = tablo.getId();
        name = tablo.getName();
        IP = tablo.getIP();
        queue = tablo.getQueue();
        comment = tablo.getComment();
        countLinesOnPage = tablo.getCountLinesOnPage();
        landscape = tablo.getLandscape();
        active = tablo.isActive();
        update = tablo.isUpdate();
        test = tablo.isTest();
        versionTabloPage = tablo.getVersionTabloPage();
        runLinesOn = tablo.isRunLinesOn();
    }

    public void updateTabloEdit(TabloEdit tabloEdit) {
        this.setQueueRepo(tabloEdit.getQueueRepo());
    }

    public QueueRepo getQueueRepo() {
        return queueRepo;
    }

    public void setQueueRepo(QueueRepo queueRepo) {
        this.queueRepo = queueRepo;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setLandscape(int landscape) {
        this.landscape = landscape;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
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

    public boolean isRunLinesOn() {
        return runLinesOn;
    }

    public void setRunLinesOn(boolean runLinesOn) {
        this.runLinesOn = runLinesOn;
    }
}
