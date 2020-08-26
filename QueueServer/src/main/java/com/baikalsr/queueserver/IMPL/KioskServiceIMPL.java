package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.UI.KioskUI;
import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.jsonView.StatusJobPrinted;
import com.baikalsr.queueserver.jsonView.MenuUnitService;
import com.baikalsr.queueserver.jsonView.ServiceList;
import com.baikalsr.queueserver.repository.KioskMenuRepo;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.repository.TicketRepo;
import com.baikalsr.queueserver.service.CreatorTicket;
import com.baikalsr.queueserver.service.KioskService;
import com.baikalsr.queueserver.service.PrintService;
import com.baikalsr.queueserver.service.Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class KioskServiceIMPL implements KioskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KioskServiceIMPL.class);

    @Autowired
    private KioskRepo kioskRepo;
    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private KioskMenuRepo kioskMenuRepo;
    @Autowired
    private CreatorTicket creatorTicket;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private PrintService printService;

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

        //Проверим принтер
        Printer printer = printService.getPrinter(kiosk);
        if (!printer.isWorking())
            return new ServiceList("Принтер не работает. Адрес принтера: " + printer.getURL());

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
                return createTalon(key, id, "-");
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

        Ticket ticket = creatorTicket.createTicket(ticketService, queue);
        Printer printer = printService.getPrinter(kiosk);

        ServiceList serviceList = new ServiceList();
        if (printer.isWorking()) {
            PrintJob printJob = printService.createPrintJob(ticket.getName());
            printTemplate(printJob, ticket);
            printJob = printer.newJob(printJob);
            if (printJob.getStatusJob() == StatusJob.WAIT) {
                serviceList.setType("Print");
                serviceList.setMessage(printJob.getNumber().toString());

            }else
                serviceList.setType("Error");
        } else {
            LOGGER.warn("Принтер недоступен. (" + printer.getURL() + ")");
            serviceList.setType("Error");
        }

        return serviceList;
    }

    @Override
    public StatusJobPrinted isPrinted(String key, UUID id) {
        Kiosk kiosk = kioskRepo.findByIP(key);
        StatusJobPrinted statusJobPrinted = new StatusJobPrinted();
        StatusJob statusJob = printService.getStatusJob(kiosk, id);
        String result = "";
        if (statusJob == StatusJob.WAIT || statusJob == StatusJob.PRINTING)
            result = "wait";
        if (statusJob == StatusJob.COMPLETE)
            result = "ok";
        else
            result = "error";
        statusJobPrinted.setPrinted(result);
        return statusJobPrinted;
    }

    private void printTemplate(PrintJob printJob, Ticket ticket) {
        try {
            printJob.addContent(printService.getCancelChines())
                    .addContent(printService.getSetWCP1251())//Установили кодировку
                    .addContent(printService.getAlign(1))
                    .addContent(printService.getKegel(1, 1))
                    .addContent("Ваш талон:".getBytes("windows-1251"))
                    .addContent(printService.getPrintAndNewLine())
                    .addContent(printService.getKegel(3, 3))
                    .addContent(ticket.getName().getBytes("windows-1251"))
                    .addContent(printService.getPrintAndFeedLines(2))
                    .addContent(printService.getKegel(0, 0))
                    .addContent(printService.getAlign(0))
                    .addContent("Перечень документов, необходимых на оформлении:".getBytes("windows-1251"))
                    .addContent(printService.getPrintAndNewLine())
                    .addContent("1. Доверенность на получение/сдачу груза.".getBytes("windows-1251"))
                    .addContent(printService.getPrintAndNewLine())
                    .addContent("2. Документ удостоверяющий личность, согласно доверенности".getBytes("windows-1251"))
                    .addContent(printService.getPrintAndNewLine())
                    .addContent("3. Сопроводительные документы на груз при отправлении от Юр. лица.".getBytes("windows-1251"))
                    .addContent(printService.getPrintAndNewLine())
                    .addContent(printService.getAlign(1))
                    .addContent("Поестителей перед вами: ZzZ".getBytes("windows-1251"))
                    .addContent(printService.getPrintAndFeedLines(5))
                    .addContent(printService.getCutPaper());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
