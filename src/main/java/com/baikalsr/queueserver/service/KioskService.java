package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.UI.KioskUI;

public interface KioskService {
    KioskUI getKioskUI(String key);
    void setCommentKiosk(String comment, String key);
}
