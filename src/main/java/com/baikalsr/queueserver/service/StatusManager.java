package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Status;

public interface StatusManager {
    Status getStatusManager(Manager manager);
    Status getStatusManager(String login);
    String statusToString(Status status);
    Boolean managerIsStartSession(Manager manager);
    int getCasement(Manager manager);
}
