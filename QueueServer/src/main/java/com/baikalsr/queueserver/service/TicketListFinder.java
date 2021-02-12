package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.service.reports.ReportBuilder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TicketListFinder extends ReportBuilder {
    

    private Date startDate;
    private Date endDate;
    private int sizePage;
    BigInteger countPages;


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

    public TicketListFinder(String name, EntityManager entityManager) {
        super(name, entityManager);
        this.entityManager = entityManager;
        this.sizePage = 10;
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
        String group = ticketSellingsField && !count ? "GROUP BY id" : "";

        baseQuery = baseQuery.replaceFirst("-FIELDS-", fields)
                .replaceFirst("-WHERE-", where)
                .replaceFirst("-GROUP-", group);

        return baseQuery;
    }

    private Query getQuery(boolean count) {
        Query query = entityManager.createNativeQuery(getQueryString(count));

        return query;
    }

    public void getList() {
        Query querySize = getQuery(true);
        BigInteger size = (BigInteger) querySize.getSingleResult();
        System.out.println(size.toString());
        countPages = size.divide(BigInteger.valueOf(sizePage));
        if (size.remainder(BigInteger.valueOf(sizePage)).compareTo(BigInteger.valueOf(0)) != 0)
            countPages.add(BigInteger.valueOf(1));

        int currentPage = 34;
        Query query = getQuery(false);
        query.setFirstResult(sizePage * (currentPage - 1));
        query.setMaxResults(Math.min(sizePage, (size.subtract(BigInteger.valueOf(sizePage * (currentPage - 1)))).intValue()));
        System.out.println("Запрос готов");
        //query.
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }
}
