package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.UI.KioskUI;
import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.jsonVeiw.MenuUnitService;
import com.baikalsr.queueserver.jsonVeiw.ServiceList;
import com.baikalsr.queueserver.repository.KioskMenuRepo;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.service.CreatorTicket;
import com.baikalsr.queueserver.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KioskServiceIMPL implements KioskService {
    @Autowired
    private KioskRepo kioskRepo;
    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private KioskMenuRepo kioskMenuRepo;
    @Autowired
    private CreatorTicket creatorTicket;

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

    @Override
    public boolean kioskRegistered(String key) {
        Kiosk kiosk = kioskRepo.findByIP(key);
        return !kiosk.getName().equals("Незарегистрированное устройство");
    }

    @Override
    public ServiceList getServices(String key) {
        return getServiceList(key, 0l);
    }

    @Override
    public ServiceList getServices(String key, Long id) {
        return getServiceList(key, id);
    }

    @Override
    public ServiceList getServices(String key, Long id, String yesNo) {
        return createTalon(key, id, yesNo);
    }

    /////////////////////////////////////////////////////////////////
    //Внутренние методы
    /////////////////////////////////////////////////////////////////

    private ServiceList getServiceList(String key, Long id) {
        Kiosk kiosk = kioskRepo.findByIP(key);
        Queue queue = queueRepo.getOne(kiosk.getQueue().getId());

        KioskMenu kioskMenuQueue = queue.getKioskMenu(); //Старшее меню очереди
        //Проверим наличие входящего меню в меню очереди
        boolean menuTrue = false;
        for (KioskMenu kioskMenu : kioskMenuQueue.getUnderKioskMenu()) {
            if (kioskMenu.getId() == id) {
                menuTrue = true; break;
            }
        }

        //Если не нашли элемент меню, то возвращаем начальный набор менюх (требуется просто обновить список)
        if (menuTrue) {
            KioskMenu kioskMenuInput = kioskMenuRepo.getOne(id);
            //Если тип новой менюшки BUTTON то возвращаем список ее подчиненных кноп
            if (kioskMenuInput.getTypeButton() == TypeButton.BUTTON)
                return getServiceListByMenuList(kioskMenuInput);
            //Если тип YESNO готовим данные для отрисовки окна с вопросом да нет
            if (kioskMenuInput.getTypeButton() == TypeButton.YESNO) {
                ServiceList serviceList = new ServiceList();
                serviceList.setType("YESNO");
                serviceList.setIdMenu(id);
                return serviceList;
            }
            if (kioskMenuInput.getTypeButton() == TypeButton.PRINT) {
                createTalon(key, id, "-");
            }
        }else {
            return getServiceListByMenuList(kioskMenuQueue);
        }
        return new ServiceList("Произошла ошибка");
    }

    private ServiceList createTalon(String key, Long id, String yesNo) {
        Kiosk kiosk = kioskRepo.findByIP(key);
        Queue queue = kiosk.getQueue();
        KioskMenu kioskMenu = kioskMenuRepo.getOne(id);
        TicketService ticketService = kioskMenu.getTicketService();
        if (!yesNo.equals("-")) {
            if (yesNo.equals("no")) ticketService = kioskMenu.getTicketService2();
        }

        creatorTicket.createTicket(ticketService, queue);

        ServiceList serviceList = new ServiceList();
        serviceList.setType("Print");

        return serviceList;
    }

    private ServiceList getServiceListByMenuList(KioskMenu kioskMenu) {
        ServiceList serviceList = new ServiceList();
        serviceList.setType("List");
        for (KioskMenu kioskMenuF : kioskMenu.getUnderKioskMenu())
            serviceList.getList().add(new MenuUnitService(kioskMenuF));
        return serviceList;
    }

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
