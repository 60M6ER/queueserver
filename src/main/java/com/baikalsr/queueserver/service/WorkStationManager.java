package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.UI.WorkStationUI;

public interface WorkStationManager {
    WorkStationUI getWorkStationUI(String key);
    WorkStationUI updateWorkStationUI(WorkStationUI workStationUI);
    void startSession(int casement, String login);
    void endSession(String login);
    void serviceClient(String login);
    void endServiceClient(String login);
    String getTypeDistributionToString();
}
