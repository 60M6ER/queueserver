package com.baikalsr.queueserver.IMPL;

import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.jsonView.workStation.*;
import com.baikalsr.queueserver.repository.*;
import com.baikalsr.queueserver.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class WorkStationManagerIMPL implements WorkStationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkStationManagerIMPL.class);


    @Autowired
    private ManagersStatusRepo managersStatusRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private ManagerTicketRepo managerTicketRepo;
    @Autowired
    private StatusManager statusManager;
    @Autowired
    private TicketServiceRepo ticketServiceRepo;
    @Autowired
    private CreatorTicket creatorTicket;
    @Autowired
    private TicketSellingRepo ticketSellingRepo;
    @Autowired
    private TicketStatusLogREPO ticketStatusLogREPO;
    @Autowired
    private ServiceLocker serviceLocker;
    @Autowired
    private SecurityService securityService;


    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public WorkStationManagerIMPL() {
    }

    @Transactional
    @Override
    public void startSession(int casement, Manager manager) {
        startSession(casement, manager, Status.WORKING_TIME);
    }

    @Transactional
    @Override
    public void startSession(int casement, Manager manager, Status status) {
        if (casement <= 0)
            throw new RuntimeException("Номер окна введен некорректно.");
        ManagersStatus managersStatus = new ManagersStatus();
        managersStatus.setCasement(casement)
                .setManager(manager)
                .setDate(new Date())
                .setStatus(status);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Начата рабочая сессия пользователя: " + manager.getName() +
                " со статусом: " + Status.getRussianName(status));
    }

    @Transactional
    @Override
    public void endSession(Manager manager) {
        ManagersStatus managersStatus = new ManagersStatus();
        managersStatus.setManager(manager)
                .setDate(new Date())
                .setStatus(Status.NOT_WORKING_TIME);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Завершена рабочая сессия пользователя: " + manager.getName());
    }

    @Transactional
    @Override
    public void startServiceClient(Manager manager) {
        Date date = new Date();
        ManagersStatus managersStatus = new ManagersStatus();
        if (statusManager.getStatusManager(manager) != Status.WAIT_CLIENT)
            throw new RuntimeException("Невозможно начать обсуживание. Текущий статус не подходит.");
        managersStatus.setCasement(statusManager.getCasement(manager))
                .setStatus(Status.SERVICING_CLIENT)
                .setDate(date)
                .setManager(manager);

        Ticket ticket = getServicingTicket(manager);
        if (ticket == null){
            throw new NullPointerException("Нет распределенных талонов!");
        }
        ticket.setDateStartService(date);
        ticket.setStatus(TicketStatus.SERVICING);

        ticketRepo.save(ticket);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Начато обслуживание клиента с талоном: " + ticket.getName()
                + " в окне №" + managersStatus.getCasement()
                + " у менеджера: "+ manager.getName());
    }

    @Override
    public boolean managerIsStartSession(Manager manager) {
        return statusManager.managerIsStartSession(manager);
    }

    @Transactional
    @Override
    public void endServiceClient(Manager manager) {
        Date date = new Date();
        ManagersStatus managersStatus = new ManagersStatus();
        Status currentStatus = statusManager.getStatusManager(manager);
        if (currentStatus != Status.SERVICING_CLIENT && currentStatus != Status.SERVICING_REGULAR_CLIENT)
            throw new RuntimeException("Пользователь не может завершить работы с талоном.");
        managersStatus.setCasement(statusManager.getCasement(manager))
                .setStatus(Status.WORKING_TIME)
                .setDate(date)
                .setManager(manager);

        Ticket ticket = getServicingTicket(manager);
        if (ticket == null){
            throw new NullPointerException("Нет распределенных талонов!");
        }
        ticket = endServiceTicket(ticket, date);
        managersStatusRepo.save(managersStatus);
        LOGGER.info("Завершено обслуживание клиента с талоном: " + ticket.getName()
                + " в окне №" + managersStatus.getCasement()
                + " у менеджера: "+ manager.getName());
    }

    private Ticket endServiceTicket(Ticket ticket, Date date) {

        if (ticket.getService().isObligatoryContractor()
            && ticket.getContractor() == null)
            throw new RuntimeException("Нельзя завершить талон с этой услугой без указания контрагента.");


        if (date == null) date = new Date();
        ticket.setDateEndService(date);
        ticket.setStatus(TicketStatus.ENDED);
        return ticketRepo.save(ticket);
    }

    @Override
    public boolean isTicketServicing(Manager manager) {
        Status status = statusManager.getStatusManager(manager);
        return status == Status.WAIT_CLIENT
                || status == Status.SERVICING_CLIENT
                || status == Status.SERVICING_REGULAR_CLIENT
                || status == Status.RECEPTION_EXPEDITION;
    }

    @Override
    public boolean isWaiting(Manager manager) {
        return statusManager.getStatusManager(manager) == Status.WAIT_CLIENT;
    }

    @Override
    public boolean isServicing(Manager manager) {
        Status status = statusManager.getStatusManager(manager);
        return status == Status.SERVICING_CLIENT
                || status == Status.SERVICING_REGULAR_CLIENT
                || status == Status.RECEPTION_EXPEDITION;
    }

    @Override
    public Ticket getServicingTicket(Manager manager) {
        Ticket ticket = ticketRepo.getServicingTicketByManger(manager.getId());

        return ticket;
    }

    @Override
    public TalonInfoPOJO getTicket(UUID id) {
        Ticket ticket = ticketRepo.getOne(id);
        if (ticket == null){
            throw new NullPointerException("Ticket not found");
        }

        return getTalonInfoFromTicket(ticket);
    }

    private TalonInfoPOJO getTalonInfoFromTicket(Ticket ticket){
        TalonInfoPOJO talonInfoPOJO = new TalonInfoPOJO();
        talonInfoPOJO.setId(ticket.getId().toString());
        talonInfoPOJO.setName(ticket.getName());
        talonInfoPOJO.setDateStartService(ticket.getDateStartService() == null ? null : simpleDateFormat.format(ticket.getDateStartService()));
        talonInfoPOJO.setService(ticket.getService().getName());
        talonInfoPOJO.setContractor(ticket.getContractor());
        talonInfoPOJO.setSellings(ticket.getTicketSellings());
        talonInfoPOJO.setStatus(ticket.getStatus());

        long timeOnPause = 0;
        List<TicketStatusLog> ticketStatusLogs = ticketStatusLogREPO.getAllByTicketOrOrderByDate(ticket.getId());

        for (int i = 0; i < ticketStatusLogs.size(); i++) {
            if (ticketStatusLogs.get(i).getTicketStatus() == TicketStatus.PAUSE) {
                if (i + 1 < ticketStatusLogs.size()) {
                    timeOnPause += ticketStatusLogs.get(i + 1).getDateLong() - ticketStatusLogs.get(i).getDateLong();
                }
            }
        }
        talonInfoPOJO.setTimeOnPause(timeOnPause);

        return talonInfoPOJO;
    }

    @Override
    public ListTalonPOJO getTicketsPaused(Manager manager) {
        List<Ticket> ticket = ticketRepo.getPausedTicketsByManger(manager.getId());
        if (ticket == null){
            throw new NullPointerException("Талоны не найдены");
        }
        ListTalonPOJO listTalonPOJO = new ListTalonPOJO();

        List<TalonInfoPOJO> talonInfoPOJOS = new ArrayList<>();
        for (int i = 0; i < ticket.size(); i++) {
            talonInfoPOJOS.add(getTalonInfoFromTicket(ticket.get(i)));
        }

        listTalonPOJO.setTickets(talonInfoPOJOS);

        return listTalonPOJO;
    }

    @Override
    public TypeDistribution getTypeDistribution(Ticket ticket) {
        if (ticket != null)
            return managerTicketRepo.getLastByTicketId(ticket.getId()).getTypeDistribution();
        return null;
    }

    @Override
    public CasementStatusPOJO getCasementStatus(Manager manager) {
        CasementStatusPOJO casementStatus = new CasementStatusPOJO();
        casementStatus.setCasement(statusManager.getCasement(manager));
        casementStatus.setNameManager(manager.getName());
        Map<String, String> currentStatus = new HashMap<>();
        Status statusC = statusManager.getStatusManager(manager);
        currentStatus.put("name", Status.getRussianName(statusC));
        currentStatus.put("value", statusC.name());
        casementStatus.setStatus(currentStatus);

        List<Map<String, String>> list = new ArrayList<>();
        for (Status status : Status.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", Status.getRussianName(status));
            map.put("value", status.name());
            list.add(map);
        }

        casementStatus.setAvailableStatuses(list);

        return casementStatus;
    }

    @Override
    public StatusesEmployeePOJO getStatusesManager(Manager manager, Manager reqManager, boolean workStation) {
        StatusesEmployeePOJO statusesEmployeePOJO = new StatusesEmployeePOJO();
        //Текущий статус
        Status currentStatus = statusManager.getStatusManager(manager);
        statusesEmployeePOJO.setCurrentStatus(currentStatus);

        Status[] statuses;

        if (workStation)
            statuses = Status.getAvailableStatuses(currentStatus);
        else {
            boolean allStatuses = securityService.testByRoleUser(Role.MANAGER, reqManager, true);

            //Массив доступных статусов
            if (allStatuses)
                statuses = Status.getAllStatusForChange();
            else
                statuses = Status.getAvailableStatuses(currentStatus);
        }

        List<TicketService> ticketServicesStatuses = ticketServiceRepo.getAllByStatusIsNotNull(); //Услуги с у казанными статусами
        List<TicketService> ticketServicesManagerStatuses = ticketServiceRepo.getAllByManagerAndStatusNotNull(manager.getId()); //Услуги менеджера с указанными статусами

        for (int i = 0; i < statuses.length; i++){
            boolean availableNewStatus = (!statusInArray(statuses[i], ticketServicesStatuses)) || (statusInArray(statuses[i], ticketServicesStatuses)
                    && statusInArray(statuses[i], ticketServicesManagerStatuses));
            if (availableNewStatus)
                statusesEmployeePOJO.addStatus(statuses[i]);
        }
        //Текущее окно
        statusesEmployeePOJO.setCasement(statusManager.getCasement(manager));

        return statusesEmployeePOJO;
    }

    @Transactional
    @Override
    public void setNewStatus(Manager manager, SetNewStatusPOJO setStatus) throws InterruptedException {
        serviceLocker.addLock(manager, false);
        Status currentStatus = statusManager.getStatusManager(manager);
        Ticket ticket = getServicingTicket(manager);
        Date date = new Date();
        boolean isEndTicket = false;
        if (ticket != null) {
            if (currentStatus == Status.SERVICING_REGULAR_CLIENT || currentStatus == Status.SERVICING_CLIENT)
                isEndTicket = true;
            else
                throw new RuntimeException("Нельзя сменить статус. У вас есть распределенный талон.");
        }

        List<TicketService> ticketServicesStatuses = ticketServiceRepo.getAllByStatusIsNotNull();
        List<TicketService> ticketServicesManagerStatuses = ticketServiceRepo.getAllByManagerAndStatusNotNull(manager.getId());

        boolean availableNewStatus = (!statusInArray(setStatus.getValueNewStatus(), ticketServicesStatuses)) || (statusInArray(setStatus.getValueNewStatus(), ticketServicesStatuses)
                && statusInArray(setStatus.getValueNewStatus(), ticketServicesManagerStatuses));

        if (!availableNewStatus)
            throw new RuntimeException("Пользователю недоступен статус для смены");


        if (setStatus.getCasement() <= 0 && setStatus.getValueNewStatus() != Status.NOT_WORKING_TIME)
            throw new RuntimeException("Некорректно заполнен номер окна.");
        if (!statusManager.isAvailableCasement(setStatus.getCasement(), manager))
            throw new RuntimeException("Данное окно не доступно для выбора");
        ManagersStatus managersStatus = new ManagersStatus();
        managersStatus.setCasement(setStatus.getCasement())
                .setManager(manager)
                .setDate(date)
                .setStatus(setStatus.getValueNewStatus());
        if (isEndTicket)
                endServiceTicket(ticket, date);
        managersStatusRepo.save(managersStatus);
        serviceLocker.unLock(manager);
        LOGGER.info("У пользователя: " + manager.getName() +
                    " изменен статус на: " + Status.getRussianName(managersStatus.getStatus()));


    }

    private boolean statusInArray(Status status, List<TicketService> array) {
        if (array == null || array.size() == 0)
            return false;

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getStatus() == status)
                return true;
        }

        return false;
    }

    @Transactional
    @Override
    public void pausedTicket(Manager manager) {


        Ticket ticket = getServicingTicket(manager);
        if (ticket == null)
            throw new RuntimeException("У пользователя не назначен талон.");

        if (!ticket.getService().isPause())
            throw new RuntimeException("Услуга талона не поддерживает постановку на паузу.");

        //Установка статуска пользователя

        SetNewStatusPOJO setNewStatusPOJO = new SetNewStatusPOJO();
        setNewStatusPOJO.setCasement(statusManager.getCasement(manager));
        setNewStatusPOJO.setValueNewStatus(Status.WORKING_TIME);

        try {
            setNewStatus(manager, setNewStatusPOJO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ticket.setStatus(TicketStatus.PAUSE);
        TicketStatusLog ticketStatusLog = new TicketStatusLog();
        ticketStatusLog.setDate(new Date());
        ticketStatusLog.setDateLong(ticketStatusLog.getDate().getTime());
        ticketStatusLog.setTicket(ticket);
        ticketStatusLog.setTicketStatus(ticket.getStatus());
        ticketRepo.save(ticket);
        ticketStatusLogREPO.save(ticketStatusLog);
        LOGGER.info("Поставлен на паузу талон: " + ticket.getName() + " пользователем " + manager.getName());
    }

    @Transactional
    @Override
    public void resumeServicingTicket(Manager manager, Ticket ticket) {
        //Установка статуска пользователя
        SetNewStatusPOJO setNewStatusPOJO = new SetNewStatusPOJO();
        setNewStatusPOJO.setCasement(statusManager.getCasement(manager));
        setNewStatusPOJO.setValueNewStatus(ticket.getService().getStatus());

        try {
            setNewStatus(manager, setNewStatusPOJO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ticket.setStatus(TicketStatus.SERVICING);
        TicketStatusLog ticketStatusLog = new TicketStatusLog();
        ticketStatusLog.setDate(new Date());
        ticketStatusLog.setDateLong(ticketStatusLog.getDate().getTime());
        ticketStatusLog.setTicket(ticket);
        ticketStatusLog.setTicketStatus(ticket.getStatus());
        ticketRepo.save(ticket);
        ticketStatusLogREPO.save(ticketStatusLog);
        LOGGER.info("Возвращен в работу талон: " + ticket.getName() + " пользователем " + manager.getName());
    }

    @Transactional
    @Override
    public StatusNewTalonPOJO createTicketPartner(Manager manager) {
        Status status = statusManager.getStatusManager(manager);
        if (status != Status.SERVICING_REGULAR_CLIENT)
            throw new RuntimeException("У пользователя должен быть статус \"Обслуживание постоянного клиента\"");

        Ticket servicingTicket = getServicingTicket(manager);
        if (servicingTicket != null)
            throw new RuntimeException("Пользователь уже обслуживает клиента.");

        Queue queue = manager.getQueue();
        TicketService ticketService = ticketServiceRepo.getFirstByStatus(Status.SERVICING_REGULAR_CLIENT);
        if (ticketService == null)
            throw new RuntimeException("Не настроены услуги для постоянного клиента");

        Ticket ticket = creatorTicket.createRegularTicket(ticketService, queue);
        Date date = new Date();
        //Связываем менеджера с талоном
        ManagerTicket managerTicket = new ManagerTicket();
        managerTicket.setManager(manager);
        managerTicket.setTicket(ticket);
        managerTicket.setDate(date);
        managerTicket.setTypeDistribution(TypeDistribution.REDIRECT);
        //Обновляем информацию о талоне
        ticket.setDateDistrib(date);
        ticket.setDateStartService(date);
        ticket.setStatus(TicketStatus.SERVICING);
        //Обновляем статус менеджера
        //setNewStatus(manager, Status.SERVICING_REGULAR_CLIENT);
        managerTicketRepo.save(managerTicket);
        ticket = ticketRepo.save(ticket);
        //managersStatusRepo.save(managersStatus);
        StatusNewTalonPOJO statusNewTalonPOJO = new StatusNewTalonPOJO();
        statusNewTalonPOJO.setStatus("ok");
        statusNewTalonPOJO.setIdTicket(ticket.getId());
        return statusNewTalonPOJO;
    }

    @Transactional
    @Override
    public void setContractorTicket(Ticket ticket, SetContractorPOJO contractorPOJO) {
//        Contractor contractor = contractorRepo.getOne(contractorPOJO.getContractor().getId());
//        if (contractor == null)
//            contractor = contractorRepo.save(contractorPOJO.getContractor());

        ticket.setContractor(contractorPOJO.getContractor());

        ticketRepo.save(ticket);
        LOGGER.info("Для талона: " + ticket.getName() + " указан контрагент: " + contractorPOJO.getContractor().getName());
    }

    @Transactional
    @Override
    public void addSellingTicket(Ticket ticket, AddSellingPojo sellingPojo) {
        TicketSelling ticketSelling = new TicketSelling();
        ticketSelling.setSellingNumber(sellingPojo.getNumberSelling());
        ticketSelling.setTicket(ticket);

        ticketSellingRepo.save(ticketSelling);
    }
}
