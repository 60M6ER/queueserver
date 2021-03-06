package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.repository.*;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.*;

@Component
public class TicketDistribution {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketDistribution.class);

    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private ManagerTicketRepo managerTicketRepo;
    @Autowired
    private TicketServiceRepo serviceRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private ManagersStatusRepo managersStatusRepo;
    @Autowired
    private ServiceLocker serviceLocker;

    private int countTickets = 0;
    private long milsec = 0;

    @Scheduled(fixedDelay = 5 * 1000)
    public synchronized void distributeTickets() {
        Long startTime = System.currentTimeMillis();
        List<Ticket> tickets = ticketRepo.findAllToDistrib();
        for (Ticket ticket : tickets) {
            Manager manager = managerRepo.getManagerByTicketServiceToDistrib(ticket.getService().getId(),
                    ticket.getQueue().getId());
            if (manager != null) {
                try {
                    if (!serviceLocker.addLock(manager, true)) {
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Date dateEdit = new Date();
                //Связываем менеджера с талоном
                ManagerTicket managerTicket = new ManagerTicket();
                managerTicket.setManager(manager);
                managerTicket.setTicket(ticket);
                managerTicket.setDate(dateEdit);
                managerTicket.setTypeDistribution(TypeDistribution.AUTO);
                //Обновляем информацию о талоне
                ticket.setDateDistrib(dateEdit);
                ticket.setStatus(TicketStatus.WAIT);
                //Обновляем статус менеджера
                ManagersStatus managersStatus = managersStatusRepo.getLastByManagerId(manager.getId());
                managersStatus.setDate(dateEdit)
                        .setStatus(Status.WAIT_CLIENT)
                        .setId(null);
                managerTicketRepo.save(managerTicket);
                ticketRepo.save(ticket);
                managersStatusRepo.save(managersStatus);
                serviceLocker.unLock(manager);
                LOGGER.info("Талон: " + ticket.getName()
                        + " {"
                        + "Очередь: " + ticket.getQueue().getName()
                        + ", Услуга: " + ticket.getService().getName()
                        + "}"
                        + " распределен для менеджера: " + manager.getName());
            }
        }
        if (tickets.size() > 0) {
            countTickets = tickets.size();
            milsec = System.currentTimeMillis() - startTime;
        }
    }

    public List<Map<String, Object>> getListTickets() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        //List<Ticket> tickets = ticketRepo.findAll();
        List<Ticket> tickets = ticketRepo.findAllSorted();
        for (Ticket ticket : tickets) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", ticket.getName());
            map.put("nameClient", ticket.getNameClient());
            map.put("dateCreate", ticket.getDateCreate());
            map.put("dateDistrib", ticket.getDateDistrib());
            map.put("dateStartService", ticket.getDateStartService());
            map.put("dateEndService", ticket.getDateEndService());
            map.put("status", ticket.getStatus());
            map.put("service", ticket.getService().getName());
            map.put("queue", ticket.getQueue().getName());
            ManagerTicket managerTicket = managerTicketRepo.getLastByTicketId(ticket.getId());
            map.put("manager", managerTicket == null ? "-" : managerTicket.getManager().getName());
            list.add(map);
        }
        return list;
    }

    public int getCountTickets() {
        return countTickets;
    }

    public long getMilsec() {
        return milsec;
    }
}
