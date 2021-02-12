package com.baikalsr.queueserver.service.reports;

import com.baikalsr.queueserver.jsonView.reports.ReportTicketNormPOJO;
import com.baikalsr.queueserver.jsonView.reports.ToFormRequestPOJO;
import com.baikalsr.queueserver.jsonView.reports.ToFormTicketListPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class ReportsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private HashMap<UUID, ReportBuilder> builders;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public UUID toFormReport(ToFormRequestPOJO toFormRequestPOJO) {
        if (toFormRequestPOJO.getStartDate() == null || toFormRequestPOJO.getEndDate() == null)
            throw new RuntimeException("Необходимо заполнить одну из дат");
        if (toFormRequestPOJO.getStartDate().compareTo(toFormRequestPOJO.getEndDate()) != -1)
            throw new RuntimeException("Дата начала должна быть больше даты окончания.");
        UUID uuid = UUID.randomUUID();
        LOGGER.info("Получено задание на формирование отчета (" + uuid + ")");
        ReportBuilder reportBuilder = new ReportTickets("Отчет " + dateFormat.format(new Date()),
                toFormRequestPOJO,
                entityManager);
        reportBuilder.setPriority(Thread.MIN_PRIORITY);
        builders.put(uuid, reportBuilder);
        reportBuilder.start();

        return uuid;
    }

    public UUID toFormTicketList(ToFormTicketListPOJO toFormTicketListPOJO) {
        if (toFormTicketListPOJO.getDateOf() == null && toFormTicketListPOJO.getDateTo() == null && toFormTicketListPOJO.getNumber() == null)
            throw new RuntimeException("Необходимо заполнить одну из дат или номер талона.");
        if (toFormTicketListPOJO.getDateOf() != null && toFormTicketListPOJO.getDateTo() != null && toFormTicketListPOJO.getDateOf().compareTo(toFormTicketListPOJO.getDateTo()) != -1)
            throw new RuntimeException("Дата начала должна быть больше даты окончания.");
        UUID uuid = UUID.randomUUID();
        LOGGER.info("Получено задание на формирование списка талонов (" + uuid + ")");
        ReportBuilder reportBuilder = new TicketListFinder("Отчет " + dateFormat.format(new Date()),
                toFormTicketListPOJO,
                entityManager);
        reportBuilder.setPriority(Thread.MIN_PRIORITY);
        builders.put(uuid, reportBuilder);
        reportBuilder.start();

        return uuid;
    }

    public Object getResult(UUID uuid) {
        if (builders.containsKey(uuid)) {
            ReportBuilder reportBuilder = builders.get(uuid);
            String status = reportBuilder.getStatusRep();
            if (status.equals("error")) {
                builders.remove(uuid);
                throw new RuntimeException("В задании произошла ошибка:\n" + reportBuilder.getMessage());
            }
            else if (status.equals("canceled")) {
                builders.remove(uuid);
                throw new RuntimeException("Задание отменено");
            }
            else if (status.equals("complete")){
                LOGGER.info("Отправлен результат отчета (" + uuid + ")");
                builders.remove(uuid);
                return reportBuilder.getResult();
            }
            else
                throw new RuntimeException("Задание выполняется");
        } else {
            throw new RuntimeException("Задания не существует");
        }
    }

    public ReportsService() {
        builders = new HashMap<>();
    }


}
