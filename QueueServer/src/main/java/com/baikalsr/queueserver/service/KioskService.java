package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.UI.KioskUI;
import com.baikalsr.queueserver.jsonView.StatusJobPrinted;
import com.baikalsr.queueserver.jsonView.ServiceList;

import java.util.UUID;

public interface KioskService {
    KioskUI getKioskUI(String key);
    void setCommentKiosk(String comment, String key);
    boolean kioskRegistered(String key);
    ServiceList getServices(String key);
    ServiceList getServices(String key, Long id);
    ServiceList getServices(String key, Long id, String yesNo);
    StatusJobPrinted isPrinted(String key, UUID id);
}
