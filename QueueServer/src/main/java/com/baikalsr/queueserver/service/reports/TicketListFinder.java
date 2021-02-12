package com.baikalsr.queueserver.service.reports;

import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.entity.TicketStatus;
import com.baikalsr.queueserver.jsonView.reports.ReportTicketListPOJO;
import com.baikalsr.queueserver.jsonView.reports.ToFormTicketListPOJO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TicketListFinder extends ReportBuilder {

    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru", "RU"));
    private final SimpleDateFormat formatDateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale("ru", "RU"));

    private ToFormTicketListPOJO toFormTicketList;
    private int sizePage;
    private BigInteger countPages;
    private int currentPage;
    private ReportTicketListPOJO reportTicketListPOJO;


    //Fields
    private boolean nameTalonField = true;
    private boolean nameClientField = true;
    private boolean dateCreateField = true;
    private boolean datePrintedField = true;
    private boolean dateDistribField = true;
    private boolean dateStartServiceField = true;
    private boolean dateEndServiceField = true;
    private boolean commentField = true;
    private boolean statusField = true;
    private boolean serviceField = true;
    private boolean queueField = true;
    private boolean contractorField = true;
    private boolean ticketSellingsField = true;
    private boolean name_managerField = true;

    private String query = "SELECT -FIELDS- FROM tickets_with_managers -WHERE- -GROUP-";
    private String querySize = "SELECT count(DISTINCT id) FROM tickets_with_managers -WHERE- -GROUP-";


    public TicketListFinder(String name, ToFormTicketListPOJO ticketListPOJO, EntityManager entityManager) {
        super(name, entityManager);
        this.sizePage = 30;
        super.status = "created";
        toFormTicketList = ticketListPOJO;
        currentPage = ticketListPOJO.getCurrentPage();

    }

    @Override
    public void run() {
        try {
            status = "running";
            Query querySize = getQuery(true);
            BigInteger size = (BigInteger) querySize.getSingleResult();
            //System.out.println(size.toString());
            countPages = size.divide(BigInteger.valueOf(sizePage));
            if (size.remainder(BigInteger.valueOf(sizePage)).compareTo(BigInteger.valueOf(0)) != 0)
                countPages = countPages.add(BigInteger.valueOf(1));

            if (currentPage == 0) {
                currentPage = 1;
            }
            if (currentPage > countPages.intValue())
                currentPage = countPages.intValue();
            reportTicketListPOJO = new ReportTicketListPOJO();
            reportTicketListPOJO.setCountPages(countPages.intValue());
            reportTicketListPOJO.setCurrentPage(currentPage);

            if (size.intValue() != 0) {
                Query query = getQuery(false);
                query.setFirstResult(sizePage * (currentPage - 1));
                query.setMaxResults(Math.min(sizePage, (size.subtract(BigInteger.valueOf(sizePage * (currentPage - 1)))).intValue()));
                //System.out.println("Запрос готов");

                List<Object[]> resultList = query.getResultList();

                for (int i = 0; i < resultList.size(); i++) {
                    reportTicketListPOJO.getLinesPOJO().add(new ReportTicketListPOJO.TicketLinePOJO());
                    reportTicketListPOJO.getLinesPOJO().get(i).setNameTalonField(getStringWithNull((String) resultList.get(i)[0]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setNameClientField(getStringWithNull((String) resultList.get(i)[1]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setDateCreateField(getDateTimeString((Date) resultList.get(i)[2]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setDatePrintedField(getDateTimeString((Date) resultList.get(i)[3]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setDateDistribField(getDateTimeString((Date) resultList.get(i)[4]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setDateStartServiceField(getDateTimeString((Date) resultList.get(i)[5]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setDateEndServiceField(getDateTimeString((Date) resultList.get(i)[6]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setCommentField(getStringWithNull((String) resultList.get(i)[7]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setStatusField(getStringWithNull(TicketStatus.getRusName(TicketStatus.values()[((Integer) resultList.get(i)[8]).intValue()])));
                    reportTicketListPOJO.getLinesPOJO().get(i).setServiceField(getStringWithNull((String) resultList.get(i)[9]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setQueueField(getStringWithNull((String) resultList.get(i)[10]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setContractorField(getStringWithNull((String) resultList.get(i)[11]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setTicketSellingsField(getStringWithNull((String) resultList.get(i)[12]));
                    reportTicketListPOJO.getLinesPOJO().get(i).setName_managerField(getStringWithNull((String) resultList.get(i)[13]));
                }
            }

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

    private String getStringWithNull(String str) {
        if (str == null) return "-";
        return str;
    }
    private String getDateTimeString(Date date) {
        if (date == null) return "-";
        return formatDateTime.format(date);
    }

    public ReportTicketListPOJO getReportTicketListPOJO() {
        return reportTicketListPOJO;
    }

    private String getQueryString(boolean count) {
        String baseQuery = count ? querySize : query;

        String fields = "";

        if (!count) {
            if (nameTalonField){
                fields += ticketSellingsField ?
                        "MAX(name) AS name," :
                        "name,";
            }
            if (nameClientField) {
                fields += ticketSellingsField ?
                        "MAX(name_client) AS name_client," :
                        "name_client,";
            }
            if (dateCreateField) {
                fields += ticketSellingsField ?
                        "MAX(date_create) AS date_create," :
                        "date_create,";
            }
            if (datePrintedField) {
                fields += ticketSellingsField ?
                        "MAX(date_printed) AS date_printed," :
                        "date_printed,";
            }
            if (dateDistribField) {
                fields += ticketSellingsField ?
                        "MAX(date_distrib) AS date_distrib," :
                        "date_distrib,";
            }
            if (dateStartServiceField) {
                fields += ticketSellingsField ?
                        "MAX(date_start_service) AS date_start_service," :
                        "date_start_service,";
            }
            if (dateEndServiceField) {
                fields += ticketSellingsField ?
                        "MAX(date_end_service) AS date_end_service," :
                        "date_end_service,";
            }
            if (commentField) {
                fields += ticketSellingsField ?
                        "MAX(comment) AS comment," :
                        "comment,";
            }
            if (statusField) {
                fields += ticketSellingsField ?
                        "MAX(status) AS status," :
                        "status,";
            }
            if (serviceField) {
                fields += ticketSellingsField ?
                        "MAX(service) AS service," :
                        "service,";
            }
            if (queueField) {
                fields += ticketSellingsField ?
                        "MAX(queue) AS queue," :
                        "queue,";
            }
            if (contractorField) {
                fields += ticketSellingsField ?
                        "MAX(contractor_name) AS contractor_name," :
                        "contractor_name,";
            }
            if (ticketSellingsField) {
                fields += "string_agg(selling_number, ', ') AS selling_number,";
            }
            if (name_managerField) {
                fields += ticketSellingsField ?
                        "MAX(name_manager) AS name_manager," :
                        "name_manager,";
            }
            fields = fields.substring(0, fields.length() - 2);
        }


        String where = "";
        boolean like = false;
        if ((toFormTicketList.getDateOf() != null && toFormTicketList.getDateTo() != null) && toFormTicketList.getNumber() != null)
            like = true;

        if (toFormTicketList.getDateOf() != null) {
            where += " date_create >= :dateOf";
        }

        if (toFormTicketList.getDateTo() != null) {
            if (where != "") where += " AND ";
            where += " date_create <= :dateTo";
        }

        if (toFormTicketList.getNumber() != null) {
            if (where != "") where += " AND ";
            if (like)
                where += " name LIKE :number || '%'";
            else
                where += " name = :number";
        }

        if (toFormTicketList.getService() != null) {
            if (where != "") where += " AND ";
            where += " ticket_service_id = :ticket_service_id";
        }

        if (toFormTicketList.getQueue() != null) {
            if (where != "") where += " AND ";
            where += " queue_id = :queue_id";
        }
        where = where == "" ? "" : " WHERE " + where;

        String group = ticketSellingsField && !count ? "GROUP BY id" : "";

        baseQuery = baseQuery.replaceFirst("-FIELDS-", fields)
                .replaceFirst("-WHERE-", where)
                .replaceFirst("-GROUP-", group);
        if (!count) baseQuery += " ORDER BY date_create";
        return baseQuery;
    }

    private Query getQuery(boolean count) {
        Query query = entityManager.createNativeQuery(getQueryString(count));

        if (toFormTicketList.getDateOf() != null) {
            query.setParameter("dateOf", toFormTicketList.getDateOf());
        }

        if (toFormTicketList.getDateTo() != null) {
            query.setParameter("dateTo", toFormTicketList.getDateTo());
        }

        if (toFormTicketList.getNumber() != null) {
            query.setParameter("number", toFormTicketList.getNumber());
        }

        if (toFormTicketList.getService() != null) {
            query.setParameter("ticket_service_id", toFormTicketList.getService().longValue());
        }

        if (toFormTicketList.getQueue() != null) {
            query.setParameter("queue_id", toFormTicketList.getQueue());
        }

        return query;
    }

    @Override
    public Object getResult() {
        return reportTicketListPOJO;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }
}
