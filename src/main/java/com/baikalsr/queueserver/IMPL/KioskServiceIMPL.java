package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.UI.KioskUI;
import com.baikalsr.queueserver.entity.Kiosk;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KioskServiceIMPL implements KioskService {
    @Autowired
    private KioskRepo kioskRepo;

    /////////////////////////////////////////////////////////////////
    //Публичные методы
    /////////////////////////////////////////////////////////////////

    public KioskUI getKioskUI(String key) {
        Kiosk kiosk = kioskRepo.findByIP(key);

        if (kiosk == null) //Если киоска нет, то создаем его как не зарегистрированный
            kiosk = createKioskByKey(key);



        return createKioskUIByKiosk(kiosk);
    }

    @Override
    public void setCommentKiosk(String comment, String key) {
        Kiosk kiosk = kioskRepo.findByIP(key);
        kiosk.setComment(comment);
        kioskRepo.save(kiosk);
    }

    /////////////////////////////////////////////////////////////////
    //Внутренние методы
    /////////////////////////////////////////////////////////////////

    private Kiosk createKioskByKey(String key) {
        Kiosk kiosk = new Kiosk();
        kiosk.setIP(key);
        kiosk.setActive(false);
        kiosk.setName("Незарегистрированное устройство");
        kioskRepo.save(kiosk);
        return kioskRepo.findByIP(key);
    }

    private KioskUI createKioskUIByKiosk(Kiosk kiosk) {
        return new KioskUI(kiosk);
    }
}
