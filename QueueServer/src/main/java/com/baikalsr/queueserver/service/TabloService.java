package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.RunLineText;
import com.baikalsr.queueserver.entity.StatusDevice;
import com.baikalsr.queueserver.entity.Tablo;
import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.jsonView.tablo.RunLinesPOJO;
import com.baikalsr.queueserver.jsonView.tablo.TabloSettingsPOJO;
import com.baikalsr.queueserver.jsonView.tablo.TicketsListForTabloPOJO;
import com.baikalsr.queueserver.repository.RunLineTextRepo;
import com.baikalsr.queueserver.repository.TabloRepo;
import com.baikalsr.queueserver.repository.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TabloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TabloService.class);


    @Autowired
    private TabloRepo tabloRepo;
    @Autowired
    private RunLineTextRepo runLineTextRepo;
    @PersistenceContext
    private EntityManager entityManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private String queryBodyInCasement = "WITH tickets AS (\n" +
            "SELECT t.id, t.name, t.date_distrib\n" +
            "\tFROM ticket t join ticket_service ts\n" +
            "\ton (t.ticket_service_id = ts.id\n" +
            "\t   and ts.display_on_tablo = true\n" +
            "\t   and (t.status = 1 or t.status = 2)\n" +
            "\t   and t.queue_id = :queueid)\n" +
            "),\n" +
            "managers_ticket_date AS (\n" +
            "\tSELECT mt.ticket_id, max(mt.date) as date\n" +
            "FROM manager_ticket mt\n" +
            "WHERE mt.ticket_id in (SELECT id\n" +
            "\t\t\t\t\t  FROM tickets)\n" +
            "GROUP BY mt.ticket_id\n" +
            "),\n" +
            "managers_tickets AS (\n" +
            "\tSELECT mt.ticket_id, mt.manager_id\n" +
            "FROM manager_ticket mt join managers_ticket_date mtd\n" +
            "on (mt.ticket_id = mtd.ticket_id\n" +
            "   and mt.date = mtd.date)\n" +
            "),\n" +
            "status_date AS (\n" +
            "\tSELECT ms.manager_id, max(ms.date) as date\n" +
            "FROM managers_status ms\n" +
            "WHERE ms.manager_id in (SELECT manager_id\n" +
            "\t\t\t\t\t   FROM managers_tickets)\n" +
            "GROUP BY ms.manager_id\n" +
            "),\n" +
            "casements AS (\n" +
            "\tSELECT ms.manager_id, ms.casement\n" +
            "FROM managers_status ms join status_date sd\n" +
            "on (ms.manager_id = sd.manager_id\n" +
            "   and ms.date = sd.date)\n" +
            ")\n" +
            "   \n" +
            "SELECT cast (t.id as varchar) as id,\n" +
            "t.name,\n" +
            "c.casement\n" +
            "FROM tickets t join managers_tickets mt\n" +
            "on (t.id = mt.ticket_id)\n" +
            "join casements c\n" +
            "on (mt.manager_id = c.manager_id) order by t.date_distrib desc";

    private String queryBodyInQueue = "select t.name " +
            "from ticket t " +
            "where t.queue_id = :queueid " +
            "   and t.status = 0 " +
            "order by t.date_create";

    /////////////////////////////////////////////////////////////////
    //Публичные методы
    /////////////////////////////////////////////////////////////////

    public TabloSettingsPOJO initTablo(String IP) {
        Tablo tablo = tabloRepo.findByIP(IP);

        if (tablo == null) //Если табло нет, то создаем его как не зарегистрированный
            tablo = createTabloByIP(IP);

        TabloSettingsPOJO tabloSettings = new TabloSettingsPOJO();
        tabloSettings.setActive(tablo.isActive());
        tabloSettings.setCountLines(tablo.getCountLinesOnPage());
        tabloSettings.setId(tablo.getId());
        tabloSettings.setLandscape(tablo.getLandscape());
        tabloSettings.setVersionTabloPage(tablo.getVersionTabloPage());

        return tabloSettings;
    }

    public TicketsListForTabloPOJO getTicketsList(String IP) {
        Tablo tablo = tabloRepo.findByIP(IP);
        if (tablo == null)
            throw new RuntimeException("Табло не зарегистрировано");

        TicketsListForTabloPOJO ticketsListForTablo = new TicketsListForTabloPOJO();
        ticketsListForTablo.setUpdate(tablo.isUpdate());
        ticketsListForTablo.setActive(tablo.isActive());
        Query queryInCasement = entityManager.createNativeQuery(queryBodyInCasement)
                .setParameter("queueid", tablo.getQueue().getId());
        Query queryInQueue = entityManager.createNativeQuery(queryBodyInQueue)
                .setParameter("queueid", tablo.getQueue().getId());

        List<Object[]> list = queryInCasement.getResultList();

        List<TicketsListForTabloPOJO.TalonToCasement> talonsToCasement = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            TicketsListForTabloPOJO.TalonToCasement talonToCasement = new TicketsListForTabloPOJO.TalonToCasement();
            talonToCasement.setId((String) list.get(i)[0]);
            talonToCasement.setName((String) list.get(i)[1]);
            talonToCasement.setCasement((Integer) list.get(i)[2]);
            talonsToCasement.add(talonToCasement);
        }

        ticketsListForTablo.setTalonsToCasement(talonsToCasement);

        List<Object> list2 = queryInQueue.getResultList();
        List<TicketsListForTabloPOJO.TalonInQueue> talonsInQueue = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            TicketsListForTabloPOJO.TalonInQueue talonInQueue = new TicketsListForTabloPOJO.TalonInQueue();
            talonInQueue.setName((String) list2.get(i));
            talonsInQueue.add(talonInQueue);
        }
        ticketsListForTablo.setTalonsInQueue(talonsInQueue);

        if (tablo.isUpdate()) {
            tablo.setUpdate(false);
            tabloRepo.save(tablo);
            LOGGER.info("Отправлен сигнал обновления для табло: " + tablo.getName() + " (" + tablo.getIP() + ")");
        }

        tablo.setStatusDevice(StatusDevice.OK);
        tablo.setErrorMessage(String.format("%s. Табло работает.", dateFormat.format(new Date())));
        tabloRepo.save(tablo);

        return ticketsListForTablo;
    }

    public RunLinesPOJO getRunLinesList(String IP) {
        Tablo tablo = tabloRepo.findByIP(IP);
        if (tablo == null)
            throw new RuntimeException("Табло не зарегистрировано");

        RunLinesPOJO runLinesPOJO = new RunLinesPOJO();
        runLinesPOJO.setActive(tablo.isRunLinesOn());
        if (tablo.isRunLinesOn()) {
            List<RunLineText> runLineTexts = runLineTextRepo.findAllForVisible(new Date());

            if (runLineTexts != null && runLineTexts.size() > 0) {
                runLinesPOJO.setRunLines(new String[runLineTexts.size()]);
                for (int i = 0; i < runLineTexts.size(); i++) {
                    runLinesPOJO.getRunLines()[i] = runLineTexts.get(i).getText();
                }
            }
        }


        return runLinesPOJO;
    }

    public void allUpdate() {
        List<Tablo> tablos = tabloRepo.findAll();
        for (int i = 0; i < tablos.size(); i++) {
            tablos.get(i).setUpdate(true);
        }
        tabloRepo.saveAll(tablos);
    }

    /////////////////////////////////////////////////////////////////
    //Внутренние методы
    /////////////////////////////////////////////////////////////////

    private Tablo createTabloByIP(String IP) {
        Tablo tablo = new Tablo();
        tablo.setIP(IP);
        tablo.setActive(false);
        tablo.setName("Незарегистрированное устройство");
        tablo = tabloRepo.save(tablo);

        LOGGER.info("Создано новое незарегистрированное устройство: табло (" + tablo.getIP() + ")");

        return tablo;
    }
}
