package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLocker {
    private List<Manager> objectsLock;

    public ServiceLocker() {
        objectsLock = new ArrayList<>();
    }

    public synchronized boolean addLock(Manager manager, boolean returnFalse) throws InterruptedException {
        while (true) {
            boolean found = false;
                for (int i = 0; i < objectsLock.size(); i++) {
                    if (objectsLock.get(i).equals(manager)){
                        manager = objectsLock.get(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    objectsLock.add(manager);
                    return true;
                }else if (returnFalse)
                    return false;
            Thread.sleep(100);
        }
    }

    public boolean unLock(Manager manager) {
        int index = -1;
        for (int i = 0; i < objectsLock.size(); i++) {
            if (objectsLock.get(i).equals(manager)){
                manager = objectsLock.get(i);
                index = i;
                break;
            }
        }
        if (index != -1) {
            objectsLock.remove(index);
        }
        return true;
    }
}
