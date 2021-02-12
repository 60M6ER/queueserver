package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TicketService;
import com.baikalsr.queueserver.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class NumeratorService {

    private final Random random = new Random();
    private final int maxNumber = 9999;
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat dayTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Autowired
    private TicketRepo ticketRepo;
    

    public String getNumber(TicketService service, Queue queue) {
        String prefix = service.getPrefix() == null ? "У" : service.getPrefix();
        String number = prefix + " ";
        Date currentDate = new Date();
        String numberInt = "";
        while (true) {
            numberInt = "" + random.nextInt(maxNumber);
            for (int i = 0; i < 4 - ("" + numberInt).length(); i++) {
                numberInt = "0" + numberInt;
            }
            try {
                List<Ticket> tickets = ticketRepo.getAllByNumberAndQueueAndStartDateAndEndDate(numberInt,
                        queue.getId(),
                        dayTimeFormat.parse(dayFormat.format(currentDate) + " 00:00:00"),
                        dayTimeFormat.parse(dayFormat.format(currentDate) + " 23:59:59"));

                if (tickets == null || tickets.size() == 0)
                    break;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        number += numberInt;
        return number;
    }
}
