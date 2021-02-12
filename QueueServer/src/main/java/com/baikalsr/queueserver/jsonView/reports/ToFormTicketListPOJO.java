package com.baikalsr.queueserver.jsonView.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ToFormTicketListPOJO {
    private final SimpleDateFormat formatDateFromJSON = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru", "RU"));
    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru", "RU"));
    private final SimpleDateFormat formatDateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale("ru", "RU"));

    private Date dateOf;
    private Date dateTo;
    private String number;
    private Long service;
    private Long queue;
    private int currentPage;


    public void setDateOf(String startDate) {
        try {
            if (startDate == null)
                this.dateOf = null;
            else {
                this.dateOf = formatDateFromJSON.parse(startDate);
                this.dateOf = formatDateTime.parse(formatDate.format(this.dateOf) + " 00:00");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDateTo(String endDate) {

        try {

            if (endDate == null) {
                endDate = null;
            }else{
                this.dateTo = formatDateFromJSON.parse(endDate);
                this.dateTo = formatDateTime.parse(formatDate.format(this.dateTo) + " 23:59");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDateOf() {
        return dateOf;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getService() {
        return service;
    }

    public void setService(Long service) {
        this.service = service;
    }

    public Long getQueue() {
        return queue;
    }

    public void setQueue(Long queue) {
        this.queue = queue;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
