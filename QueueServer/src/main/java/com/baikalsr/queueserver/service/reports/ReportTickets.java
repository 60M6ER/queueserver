package com.baikalsr.queueserver.service.reports;

import com.baikalsr.queueserver.jsonView.reports.ReportTicketNormPOJO;
import com.baikalsr.queueserver.jsonView.reports.ToFormRequestPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ReportTickets extends ReportBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(ReportTickets.class);
    private ReportTicketNormPOJO reportTicketNormPOJO;
    private ToFormRequestPOJO toFormRequestPOJO;


    private String queryAllTickets = "SELECT\n" +
            "    cast (t.id as varchar) as id,\n" +
            "    t.date_create,\n" +
            "    t.date_start_service,\n" +
            "    t.name,\n" +
            "    q.name AS nameQueue,\n" +
            "    date_part('week', t.date_create) AS week,\n" +
            "    date_part('day', t.date_create) AS day\n" +
            "FROM ticket t join queue q on t.queue_id = q.id\n" +
            "    AND t.date_create >= :date_start \n" +
            "    AND t.date_create <= :date_end \n" +
            "    AND t.date_start_service is not null \n" +
            "order by q.name, week, day;";

    public ReportTickets(String name, ToFormRequestPOJO toFormRequestPOJO, EntityManager entityManager) {
        super(name, entityManager);
        this.toFormRequestPOJO = toFormRequestPOJO;
        reportTicketNormPOJO = null;
        super.status = "created";
    }

    @Override
    public void run() {
        try {
            status = "running";
            Query queryInCasement = entityManager.createNativeQuery(queryAllTickets)
                    .setParameter("date_start", toFormRequestPOJO.getStartDate())
                    .setParameter("date_end", toFormRequestPOJO.getEndDate());

            reportTicketNormPOJO = new ReportTicketNormPOJO(queryInCasement.getResultList(), toFormRequestPOJO);

            status = "complete";
        }  catch (RuntimeException e) {
            if (e.getClass().equals(InterruptedException.class)) {
                status = "canceled";
            }else {
                status = "error";
                message = e.getMessage();
                e.printStackTrace();
            }
        }

    }

    public ReportTicketNormPOJO getReportTicketNormPOJO() {
        return reportTicketNormPOJO;
    }

    @Override
    public Object getResult() {
        return reportTicketNormPOJO;
    }
}
