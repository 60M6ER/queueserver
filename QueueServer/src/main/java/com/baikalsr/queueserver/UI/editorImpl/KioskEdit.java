package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KioskEdit {

    @Autowired QueueRepo queueRepo;

    private String UUID;
    private Long id;
    private String name;
    private String IP;
    private Queue queue;
    private String comment;
    private boolean test;
    private boolean active;

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

    public KioskEdit() {
        UUID = java.util.UUID.randomUUID().toString();
    }


    public KioskEdit cloneObj(){
        KioskEdit kioskEdit = new KioskEdit();
        kioskEdit.setQueueRepo(this.queueRepo);
        return kioskEdit;
    }

    public KioskEdit(Kiosk kiosk) {
        UUID = java.util.UUID.randomUUID().toString();
        id = kiosk.getId();
        name = kiosk.getName();
        IP = kiosk.getIP();
        queue = kiosk.getQueue();
        comment = kiosk.getComment();
        test = kiosk.isTest();
    }

    public void rebuildKioskEdit(Kiosk kiosk) {
        id = kiosk.getId();
        name = kiosk.getName();
        IP = kiosk.getIP();
        queue = kiosk.getQueue();
        comment = kiosk.getComment();
        test = kiosk.isTest();
    }

    public void updateKioskEdit(KioskEdit kioskEdit) {
        this.setQueueRepo(kioskEdit.getQueueRepo());
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
