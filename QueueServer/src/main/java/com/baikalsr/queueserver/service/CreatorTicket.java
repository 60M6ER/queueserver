package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.IMPL.WorkStationManagerIMPL;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TicketService;
import com.baikalsr.queueserver.entity.TicketStatus;
import com.baikalsr.queueserver.repository.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreatorTicket {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatorTicket.class);

    @Autowired
    private TicketRepo ticketRepo;

    private int indexTicket = 1;

    public Ticket createTicket(TicketService service, Queue queue) {
        Ticket ticket = new Ticket();
        ticket.setName(service.getName().substring(0,2).toUpperCase() + " " + (indexTicket++));
        ticket.setDateCreate(new Date());
        ticket.setStatus(TicketStatus.PRINTING);
        ticket.setQueue(queue);
        ticket.setService(service);



        ticketRepo.save(ticket);
        LOGGER.info("Создан талон: " + ticket.getName()
            + " {Очередь: " + queue.getName()
            + ", Услуга: " + service.getName()
            + "}");
        return ticket;
    }
}
