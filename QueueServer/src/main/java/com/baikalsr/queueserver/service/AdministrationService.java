package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.exceptions.ManagerNotFound;
import com.baikalsr.queueserver.exceptions.QueueNotFound;
import com.baikalsr.queueserver.jsonView.administration.*;
import com.baikalsr.queueserver.jsonView.workStation.SetNewStatusPOJO;
import com.baikalsr.queueserver.jsonView.workStation.StatusesEmployeePOJO;
import com.baikalsr.queueserver.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AdministrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationService.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private TicketServiceRepo ticketServiceRepo;
    @Autowired
    private WorkStationManager workStationManager;
    @Autowired
    private KioskRepo kioskRepo;
    @Autowired
    private TabloRepo tabloRepo;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private TicketRepo ticketRepo;
    @PersistenceContext
    private EntityManager entityManager;

    private String ticketListQuery = "SELECT cast (id as varchar) as id, date_create, date_distrib, date_end_service, date_start_service, name, status, queue_id, name_manager, casement\n" +
            "\tFROM public.tickets_queue_and_statuses_managers " +
            "where queue_id = :queue_id ;";
    private String managersListQuery = "SELECT cast (id as varchar) as id, " +
            "loginad,\n" +
            "name,\n" +
            "queue_id,\n" +
            "date,\n" +
            "casement,\n" +
            "status, " +
            "CASE status " +
            "WHEN 'NOT_WORKING_TIME' " +
            "THEN 2 " +
            "WHEN 'INDIVIDUAL_TIME' " +
            "THEN 0 " +
            "WHEN 'WAIT_CLIENT' " +
            "THEN 0 " +
            "WHEN 'SERVICING_CLIENT' " +
            "THEN 1 " +
            "WHEN 'SERVICING_REGULAR_CLIENT' " +
            "THEN 1 " +
            "WHEN 'WORKING_TIME' " +
            "THEN 1 " +
            "WHEN 'RECEPTION_EXPEDITION' " +
            "THEN 1 " +
            "ELSE 2" +
            "END AS statusSort " +
            "" +
            "\tFROM public.managers_statuses " +
            "where queue_id = :queue_id " +
            "ORDER BY statusSort, status, date DESC, name;";

    public StatusPageAdPOJO getTicketList(Queue queue) {

        StatusPageAdPOJO statusPageAdPOJO = new StatusPageAdPOJO();

        Query queryTalonsList = entityManager.createNativeQuery(ticketListQuery)
                .setParameter("queue_id", queue.getId());
        Query queryManagersList = entityManager.createNativeQuery(managersListQuery)
                .setParameter("queue_id", queue.getId());

        List<Object[]> list = queryTalonsList.getResultList();

        for (int i = 0; i < list.size(); i++) {
            StatusPageAdPOJO.TicketAd ticketAd = new StatusPageAdPOJO.TicketAd();
            ticketAd.setCasement(list.get(i)[9] == null ? "-" :  Integer.toString((int)list.get(i)[9]));
            ticketAd.setTicket((String) list.get(i)[5]);
            ticketAd.setStatus((int) list.get(i)[6]);
            ticketAd.setStatusName(TicketStatus.getRusName(TicketStatus.values()[(int) list.get(i)[6]]));
            ticketAd.setManager(list.get(i)[9] == null ? "-" : (String) list.get(i)[8]);
            ticketAd.setDateQueue(((Date) list.get(i)[1]) == null ? "-" : dateFormat.format((Date) list.get(i)[1]));
            ticketAd.setDateDistrib(((Date) list.get(i)[2]) == null ? "-" : dateFormat.format((Date) list.get(i)[2]));
            ticketAd.setDateService(((Date) list.get(i)[4]) == null ? "-" : dateFormat.format((Date) list.get(i)[4]));
            statusPageAdPOJO.addTicket(ticketAd);
        }

        list = queryManagersList.getResultList();

        long currentDate = new Date().getTime();

        for (int i = 0; i < list.size(); i++) {
            StatusPageAdPOJO.ManagerAd managerAd = new StatusPageAdPOJO.ManagerAd();
            managerAd.setLoginad((String) list.get(i)[1]);
            managerAd.setName((String) list.get(i)[2]);
            managerAd.setDate((list.get(i)[4]) == null ? "-" : dateFormat.format((Date) list.get(i)[4]));
            managerAd.setCasement(list.get(i)[5] == null ? "-" :  Integer.toString((int)list.get(i)[5]));
            managerAd.setStatus(list.get(i)[6] == null ? Status.NOT_WORKING_TIME.name() : (String) list.get(i)[6]);
            managerAd.setStatusName(Status.getRussianName(Status.valueOf(managerAd.getStatus())));
            managerAd.setTime(managerAd.getDate().equals("-") ? "-" : getTimeOnStatus((Date) list.get(i)[4], currentDate));

            statusPageAdPOJO.addManager(managerAd);
        }

        //List<Kiosk> kiosks = kioskRepo.findAllByQueueAndTestOrderById(queue, false);
        List<Kiosk> kiosks = kioskRepo.findAllByQueueForAdminPage(queue.getId());
        for (int i = 0; i < kiosks.size(); i++) {
            StatusPageAdPOJO.KioskPOJO kioskPOJO = new StatusPageAdPOJO.KioskPOJO();
            kioskPOJO.setName(kiosks.get(i).getName());
            kioskPOJO.setStatusDevice(kiosks.get(i).getStatusDevice());
            kioskPOJO.setMessage(kiosks.get(i).getErrorMessage());

            if (!kiosks.get(i).isActive()) {
                kioskPOJO.setStatusDevice(StatusDevice.NOT_OK);
                kioskPOJO.setMessage("Киоск отключен в настройках");
            }

            statusPageAdPOJO.getKiosks().add(kioskPOJO);
        }

        //List<Tablo> tablos = tabloRepo.findAllByQueueOrderById(queue);
        List<Tablo> tablos = tabloRepo.findAllByQueueForAdmin(queue.getId());
        for (int i = 0; i < tablos.size(); i++) {
            StatusPageAdPOJO.TabloPOJO tabloPOJO = new StatusPageAdPOJO.TabloPOJO();
            tabloPOJO.setName(tablos.get(i).getName());
            tabloPOJO.setStatusDevice(tablos.get(i).getStatusDevice());
            tabloPOJO.setMessage(tablos.get(i).getErrorMessage());

            if (!tablos.get(i).isActive()) {
                tabloPOJO.setStatusDevice(StatusDevice.NOT_OK);
                tabloPOJO.setMessage("Табло отключено в настройках");
            }

            statusPageAdPOJO.getTablos().add(tabloPOJO);
        }

        return statusPageAdPOJO;
    }

    public ManagersListsPOJO getListsManagers(Queue queue) {
        List<Manager> listWithQueue = managerRepo.findAllByQueueAndActiveOrderByName(queue, true);
        List<Manager> listWithoutQueue = managerRepo.findAllWithoutQueueOrderByName();
        ManagersListsPOJO managersListsPOJO = new ManagersListsPOJO();
        for (int i = 0; i < listWithQueue.size(); i++) {
            ManagersListsPOJO.ManagerPOJO managerPOJO = new ManagersListsPOJO.ManagerPOJO();
            managerPOJO.setCount(i + 1);
            managerPOJO.setId(listWithQueue.get(i).getId());
            managerPOJO.setName(listWithQueue.get(i).getName());
            managersListsPOJO.addManagerWithQueue(managerPOJO);
        }
        for (int i = 0; i < listWithoutQueue.size(); i++) {
            ManagersListsPOJO.ManagerPOJO managerPOJO = new ManagersListsPOJO.ManagerPOJO();
            managerPOJO.setCount(i + 1);
            managerPOJO.setId(listWithoutQueue.get(i).getId());
            managerPOJO.setName(listWithoutQueue.get(i).getName());
            managersListsPOJO.addManagerWithoutQueue(managerPOJO);
        }

        return managersListsPOJO;
    }

    public ManagerDataPOJO getManagerData(Manager manager, Manager currentManager){
        ManagerDataPOJO managerDataPOJO = new ManagerDataPOJO();
        managerDataPOJO.setManagerID(manager.getId());
        managerDataPOJO.setNameManager(manager.getName());
        managerDataPOJO.setStatuses(workStationManager.getStatusesManager(manager, currentManager, false));
        managerDataPOJO.setQueues(getQueues(manager, currentManager == null ? manager : currentManager, false));

        List<TicketService> ticketServices = ticketServiceRepo.getAllOrderByName();
        for (int i = 0; i < ticketServices.size(); i++) {
            SelectedServicePOJO servicePOJO = new SelectedServicePOJO();
            servicePOJO.setId(ticketServices.get(i).getId());
            servicePOJO.setName(ticketServices.get(i).getName());
            servicePOJO.setSelected(ticketServices.get(i).getManagers().contains(manager));
            managerDataPOJO.addService(servicePOJO);
        }

        return managerDataPOJO;
    }

    public QueuesPOJO getQueues(Manager manager, Manager requestManager, boolean onlyAdm) {

        QueuesPOJO queuesPOJO = new QueuesPOJO();
        queuesPOJO.setQueues(new ArrayList<>());
        if (securityService.isAdministrator(requestManager)){
            List<Queue> queues = queueRepo.findAll();
            queuesPOJO.setCurrent_queue(manager.getQueue() == null ? -1 : manager.getQueue().getId());
            for (int i = 0; i < queues.size(); i++) {
                QueuesPOJO.QueuePOJO queue = new QueuesPOJO.QueuePOJO();
                queue.setName(queues.get(i).getName());
                queue.setValue(queues.get(i).getId());
                queuesPOJO.getQueues().add(queue);
            }
        } else {
            if (manager.getQueue() != null) {
                queuesPOJO.setCurrent_queue(manager.getQueue().getId());
                QueuesPOJO.QueuePOJO queue = new QueuesPOJO.QueuePOJO();
                queue.setName(manager.getQueue().getName());
                queue.setValue(manager.getQueue().getId());
                queuesPOJO.getQueues().add(queue);
            }
        }

        return queuesPOJO;
    }

    @Transactional
    public void saveDataManager(SaveManagersPOJO saveManagersPOJO) {
        Manager manager = managerRepo.getOne(saveManagersPOJO.getManagerID());
        if (manager == null)
            throw new ManagerNotFound(saveManagersPOJO.getManagerID());

        Ticket ticket = workStationManager.getServicingTicket(manager);
        if (ticket != null) {
            if (ticket.getStatus().equals(TicketStatus.SERVICING) || ticket.getStatus().equals(TicketStatus.WAIT)){
                if (!saveManagersPOJO.getCurrentStatus().equals(Status.WAIT_CLIENT.name()) && !saveManagersPOJO.getCurrentStatus().equals(Status.SERVICING_CLIENT.name())){
                    ticket.setStatus(TicketStatus.ENDED);
                    ticket.setDateStartService(ticket.getDateStartService() == null ? new Date() : ticket.getDateStartService());
                    ticket.setDateEndService(ticket.getDateEndService() == null ? new Date() : ticket.getDateEndService());
                    ticket = ticketRepo.save(ticket);
                }
            }
        }

        ManagerDataPOJO managerDataPOJOOld = getManagerData(manager, null);
        if (!saveManagersPOJO.getCurrentStatus().equals(
                managerDataPOJOOld.getStatuses().getCurrentStatus().getValue().name()) ||
                !(saveManagersPOJO.getCasement() == managerDataPOJOOld.getStatuses().getCasement())) {
            setNewStatus(manager, saveManagersPOJO);
        }

        if (saveManagersPOJO.getCurrent_queue() !=
                managerDataPOJOOld.getQueues().getCurrent_queue()) {
            setNewQueue(manager, saveManagersPOJO.getCurrent_queue());
        }

        if (!(saveManagersPOJO.getServices().size() == managerDataPOJOOld.getServices().size()))
            throw new RuntimeException("Сохраниение услуг невозможно, необходимо обновить страницу");

        ArrayList<TicketService> services = new ArrayList<>();
        for (int i = 0; i < saveManagersPOJO.getServices().size(); i++) {
            if (saveManagersPOJO.getServices().get(i).getId() ==
                    managerDataPOJOOld.getServices().get(i).getId()) {
                if (saveManagersPOJO.getServices().get(i).isSelected() != managerDataPOJOOld.getServices().get(i).isSelected()) {
                    TicketService ticketService = ticketServiceRepo.getOne(saveManagersPOJO.getServices().get(i).getId());
                    if (saveManagersPOJO.getServices().get(i).isSelected())
                        addManager(ticketService, manager);
                    else
                        delManager(ticketService, manager);

                    services.add(ticketService);
                }
            } else {
                throw new RuntimeException("Сохраниение услуг невозможно, необходимо обновить страницу");
            }
        }
        ticketServiceRepo.saveAll(services);
    }

    public void saveDataQueue(QueueDataPOJO queueDataPOJO) {
        Queue queue = queueRepo.getOne(queueDataPOJO.getQueueId());
        if (queue == null) {
            throw new QueueNotFound(queueDataPOJO.getQueueId());
        }

        if (queue.isActive() != queueDataPOJO.isActive() ||
                queue.getQuantityInform() != queueDataPOJO.getQuantityInform() ||
                queue.getTimeInform() != queueDataPOJO.getTimeInform()) {
            queue.setActive(queueDataPOJO.isActive());
            queue.setQuantityInform(queueDataPOJO.getQuantityInform());
            queue.setTimeInform(queueDataPOJO.getTimeInform());
            queueRepo.save(queue);
        }
        QueueDataPOJO queueDataPOJOOld = getQueueData(queue);
        if (queueDataPOJO.getKiosks().size() != queueDataPOJOOld.getKiosks().size())
            throw new RuntimeException("Сохраниение киосков невозможно, необходимо обновить страницу");

        if (queueDataPOJO.getTablos().size() != queueDataPOJOOld.getTablos().size())
            throw new RuntimeException("Сохраниение табло невозможно, необходимо обновить страницу");

        ArrayList<Kiosk> kiosks = new ArrayList<>();
        for (int i = 0; i < queueDataPOJO.getKiosks().size(); i++) {
            if (queueDataPOJO.getKiosks().get(i).getId() != queueDataPOJOOld.getKiosks().get(i).getId())
                throw new RuntimeException("Сохраниение киосков невозможно, необходимо обновить страницу");
            if (queueDataPOJO.getKiosks().get(i).isActive() != queueDataPOJOOld.getKiosks().get(i).isActive()) {
                Kiosk kiosk = kioskRepo.getOne(queueDataPOJO.getKiosks().get(i).getId());
                kiosk.setActive(queueDataPOJO.getKiosks().get(i).isActive());
                kiosks.add(kiosk);
            }
        }
        ArrayList<Tablo> tablos = new ArrayList<>();
        for (int i = 0; i < queueDataPOJO.getTablos().size(); i++) {
            if (queueDataPOJO.getTablos().get(i).getId() != queueDataPOJOOld.getTablos().get(i).getId())
                throw new RuntimeException("Сохраниение киосков невозможно, необходимо обновить страницу");
            if (queueDataPOJO.getTablos().get(i).isActive() != queueDataPOJOOld.getTablos().get(i).isActive() ||
                    queueDataPOJO.getTablos().get(i).getCountLinesOnPage() != queueDataPOJOOld.getTablos().get(i).getCountLinesOnPage()) {
                Tablo tablo = tabloRepo.getOne(queueDataPOJO.getTablos().get(i).getId());
                tablo.setActive(queueDataPOJO.getTablos().get(i).isActive());
                tablo.setCountLinesOnPage(queueDataPOJO.getTablos().get(i).getCountLinesOnPage());
                tablos.add(tablo);
            }
        }

        kioskRepo.saveAll(kiosks);
        tabloRepo.saveAll(tablos);
    }

    public QueueDataPOJO getQueueData(Queue queue) {
        QueueDataPOJO queueDataPOJO = new QueueDataPOJO();
        queueDataPOJO.setActive(queue.isActive());
        queueDataPOJO.setQuantityInform(queue.getQuantityInform());
        queueDataPOJO.setTimeInform(queue.getTimeInform());

        List<Kiosk> kiosks = kioskRepo.findAllByQueueAndTestOrderById(queue, false);
        List<Tablo> tablos = tabloRepo.findAllByQueueOrderById(queue);

        for (int i = 0; i < kiosks.size(); i++) {
            QueueDataPOJO.KioskPOJO kiosk = new QueueDataPOJO.KioskPOJO();
            kiosk.setId(kiosks.get(i).getId());
            kiosk.setName(kiosks.get(i).getName());
            kiosk.setComment(kiosks.get(i).getComment());
            kiosk.setActive(kiosks.get(i).isActive());
            queueDataPOJO.getKiosks().add(kiosk);
        }

        for (int i = 0; i < tablos.size(); i++) {
            QueueDataPOJO.TabloPOJO tablo = new QueueDataPOJO.TabloPOJO();
            tablo.setId(tablos.get(i).getId());
            tablo.setName(tablos.get(i).getName());
            tablo.setComment(tablos.get(i).getComment());
            tablo.setActive(tablos.get(i).isActive());
            tablo.setCountLinesOnPage(tablos.get(i).getCountLinesOnPage());
            queueDataPOJO.getTablos().add(tablo);
        }


        return queueDataPOJO;
    }

    private TicketService delManager(TicketService ticketService, Manager manager) {
        List<Manager> managers = new ArrayList<>();
        for (int i = 0; i < ticketService.getManagers().size(); i++) {
            if (!(ticketService.getManagers().get(i).equals(manager)))
                managers.add(ticketService.getManagers().get(i));
        }
        ticketService.setManagers(managers);
        return ticketService;
    }

    private TicketService addManager(TicketService ticketService, Manager manager) {
        ticketService.getManagers().add(manager);
        return ticketService;
    }

    private void setNewStatus(Manager manager, SaveManagersPOJO saveManagersPOJO) {
        SetNewStatusPOJO setNewStatusPOJO = new SetNewStatusPOJO();
        setNewStatusPOJO.setValueNewStatus(Status.valueOf(saveManagersPOJO.getCurrentStatus()));
        setNewStatusPOJO.setIdUser(manager.getLoginAD());
        setNewStatusPOJO.setCasement(saveManagersPOJO.getCasement());
        try {
            workStationManager.setNewStatus(manager, setNewStatusPOJO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setNewQueue(Manager manager, long queueId) {
        Queue queue = null;
        if (queueId != -1) {

            queue = queueRepo.getOne(queueId);
            if (queue == null)
                throw new QueueNotFound(queueId);
        }

        manager.setQueue(queue);
        managerRepo.save(manager);
    }

    private String getTimeOnStatus(Date date, long currentDate) {
        long dateLine = date.getTime();
        long sub = currentDate - dateLine;
        sub = sub / 1000;
        long hours = sub / (60*60);
        long residue = sub % (60*60);
        long minutes = residue / (60);
        long seconds = residue % (60);
        return String.format("%s%s:%s%s:%s%s", hours < 10 ? "0" : "",
                hours,
                minutes < 10 ? "0" : "",
                minutes,
                seconds < 10 ? "0" : "",
                seconds);
    }
}
