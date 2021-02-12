package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.UI.KioskUI;
import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.jsonView.StatusJobPrinted;
import com.baikalsr.queueserver.jsonView.MenuUnitService;
import com.baikalsr.queueserver.jsonView.ServiceList;
import com.baikalsr.queueserver.repository.*;
import com.baikalsr.queueserver.service.CreatorTicket;
import com.baikalsr.queueserver.service.KioskService;
import com.baikalsr.queueserver.service.PrintService;
import com.baikalsr.queueserver.service.Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class KioskServiceIMPL implements KioskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KioskServiceIMPL.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
    private TicketServiceRepo ticketServiceRepo;
    @Autowired
    private PrintService printService;

    private TicketService bsService;

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
        if (!kiosk.isTest() && !printer.isWorking())
            return new ServiceList("Принтер не работает. Адрес принтера: " + printer.getURL());

        if (printer.getStatusPaper() == StatusPrinter.PRINT_HEAD_BAD) {
            kiosk.setStatusDevice(StatusDevice.NOT_OK);
            kiosk.setErrorMessage(String.format("%s.Перегрев печатающей головки", dateFormat.format(new Date())));
        }else if (printer.getStatusPaper() == StatusPrinter.CUTTER_ERROR) {
            kiosk.setStatusDevice(StatusDevice.NOT_OK);
            kiosk.setErrorMessage(String.format("%s.Проблемы с ножом", dateFormat.format(new Date())));
        }else if  (printer.getStatusPaper() == StatusPrinter.WILL_NO_PAPER) {
            kiosk.setStatusDevice(StatusDevice.MAYBE_NOT_OK);
            kiosk.setErrorMessage(String.format("%s.Скоро закончится бумага", dateFormat.format(new Date())));
        }else if  (printer.getStatusPaper() == StatusPrinter.NO_PAPER) {
            kiosk.setStatusDevice(StatusDevice.NOT_OK);
            kiosk.setErrorMessage(String.format("%s.Закончилась бумага", dateFormat.format(new Date())));
        }else {
            kiosk.setStatusDevice(StatusDevice.OK);
            kiosk.setErrorMessage(String.format("%s. Принтер работает нормально", dateFormat.format(new Date())));
        }
        kioskRepo.save(kiosk);

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
                serviceList.setIdBSService(getBsService().getId());
                return serviceList;
            }
            if (kioskMenuInput.getTypeButton() == TypeButton.PRINT) {
                return createTalon(key, id, "-");
            }
        }else {
            if (getBsService()!= null && getBsService().getId().equals(id)) {
                return createTalon(key, id, "-");
            }
            return getServiceListByMenuList(kioskMenuQueue);
        }
        return new ServiceList("Произошла ошибка");
    }

    private ServiceList createTalon(String key, Long id, String yesNo) {
        Kiosk kiosk = kioskRepo.findByIP(key);
        Queue queue = kiosk.getQueue();
        TicketService ticketService;
        KioskMenu kioskMenu = null;
        if (getBsService()!= null && getBsService().getId().equals(id)) {
            ticketService = ticketServiceRepo.getOne(id);
            yesNo = "-";
        }else {
            kioskMenu = kioskMenuRepo.getOne(id);
            ticketService = kioskMenu.getTicketService();
        }

        if (!yesNo.equals("-")) {
            if (yesNo.equals("no")) ticketService = kioskMenu.getTicketService2();
        }

        Printer printer = printService.getPrinter(kiosk);

        ServiceList serviceList = new ServiceList();
        if (printer.isWorking() || kiosk.isTest()) {
            Ticket ticket = creatorTicket.createTicket(ticketService, queue);
            PrintJob printJob = printService.createPrintJob(ticket.getName());
            printTemplate(printJob, ticket);
            if (!kiosk.isTest())
                printJob = printer.newJob(printJob);
            ticket.setPrintJobID(printJob.getNumber());
            if (printJob.getStatusJob() == StatusJob.WAIT || printJob.getStatusJob() == StatusJob.PRINTING || kiosk.isTest()) {
                serviceList.setType("Print");
                serviceList.setMessage(printJob.getNumber().toString());
                ticket.setStatus(TicketStatus.PRINTING);
            }else {
                serviceList.setType("Error");
                ticket.setStatus(TicketStatus.ERROR_PRINT);
                ticket.setDatePrinted(new Date());
            }
            ticketRepo.save(ticket);
        } else {
            LOGGER.warn("Принтер неработает. (" + printer.getURL() + ")");
            serviceList.setType("Error");
        }

        return serviceList;
    }

    @Override
    public StatusJobPrinted isPrinted(String key, UUID id) {
        Kiosk kiosk = kioskRepo.findByIP(key);
        Ticket ticket = ticketRepo.getTicketByPrintJobID(id);
        StatusJobPrinted statusJobPrinted = new StatusJobPrinted();

        if (kiosk.isTest()) {
            statusJobPrinted.setPrinted("ok");
            return statusJobPrinted;
        }

        StatusJob statusJob = printService.getStatusJob(kiosk, id);
        String result = "";
        if (statusJob == StatusJob.WAIT || statusJob == StatusJob.PRINTING){
            result = "wait";
            ticket.setStatus(TicketStatus.PRINTING);
        }
        if (statusJob == StatusJob.COMPLETE){
            result = "ok";
            ticket.setStatus(TicketStatus.QUEUE);
            ticket.setDatePrinted(new Date());
        }
        else{
            result = "error";
            ticket.setStatus(TicketStatus.ERROR_PRINT);
            ticket.setDatePrinted(new Date());
        }
        ticketRepo.save(ticket);
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
                    .addContent(printService.getAlign(0));
            if (ticket.getService().getTypeStr() == TypeService.SHIPMENT) {
                printJob.addContent("Перечень документов, необходимых на оформлении:".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine())
                        .addContent("1. Документ удостоверяющий личность.".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine())
                        .addContent("2. Сопроводительные документы на груз при отправлении от Юр. лица.".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine());
            }else if (ticket.getService().getTypeStr() == TypeService.RECEPTION) {
                printJob.addContent("Перечень документов, необходимых на оформлении:".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine())
                        .addContent("1. Доверенность на получение груза.".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine())
                        .addContent("2. Документ удостоверяющий личность, согласно доверенности.".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine());
            }else { //if (ticket.getService().getTypeStr() == TypeService.SHIPandRECEP) {
                printJob.addContent("Перечень документов, необходимых на оформлении:".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine())
                        .addContent("1. Доверенность на получение/сдачу груза.".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine())
                        .addContent("2. Документ удостоверяющий личность, согласно доверенности.".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine())
                        .addContent("3. Сопроводительные документы на груз при отправлении от Юр. лица.".getBytes("windows-1251"))
                        .addContent(printService.getPrintAndNewLine());
            }

            printJob.addContent(printService.getAlign(1))
                    .addContent(("Посетителей перед вами: " + getClientBeforeClient(ticket)).getBytes("windows-1251"))
                    .addContent(printService.getPrintAndFeedLines(5))
                    .addContent(printService.getCutPaper());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private int getClientBeforeClient(Ticket ticket) {
        List<Ticket> tickets = ticketRepo.findAllInQueueByQueue(ticket.getQueue().getId(), ticket.getId());
        for (int i = 0; i < tickets.size(); i++) {
            if (ticket.getId().equals(tickets.get(i).getId()))
                return i;
        }
        return tickets.size();
    }

    private ServiceList getServiceListByMenuList(KioskMenu kioskMenu) {
        ServiceList serviceList = new ServiceList();
        serviceList.setType("List");
        for (KioskMenu kioskMenuF : kioskMenu.getUnderKioskMenu())
            serviceList.getList().add(new MenuUnitService(kioskMenuF));
        serviceList.setIdBSService(getBsService() == null ? null : getBsService().getId());
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

    private TicketService getBsService() {
        if (bsService == null) {
            bsService = ticketServiceRepo.getFirstByBSService(true);
        }

        return bsService;
    }

    private KioskUI createKioskUIByKiosk(Kiosk kiosk) {
        return new KioskUI(kiosk);
    }
}
