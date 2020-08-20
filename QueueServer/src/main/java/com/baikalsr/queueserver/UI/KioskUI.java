package com.baikalsr.queueserver.UI;

import com.baikalsr.queueserver.entity.Kiosk;
import com.baikalsr.queueserver.entity.KioskMenu;

import java.util.List;

public class KioskUI {
    private boolean registered;
    private List<KioskMenu> menus;
    private String comment;

    public KioskUI() {
    }

    public KioskUI(Kiosk kiosk) {
        this.registered = !kiosk.getName().equals("Незарегистрированное устройство");
        this.comment = kiosk.getComment();
    }


    /////////////////////////////////////////////////////////////////
    //Get/Input methods
    /////////////////////////////////////////////////////////////////

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public List<KioskMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<KioskMenu> menus) {
        this.menus = menus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
