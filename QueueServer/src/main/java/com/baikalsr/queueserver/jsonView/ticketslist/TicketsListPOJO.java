package com.baikalsr.queueserver.jsonView.ticketslist;


public class TicketsListPOJO {
    private String nameTalon;
    private String nameClient;
    private String dateCreate;
    private String datePrinted;
    private String dateDistrib;
    private String dateStartService;
    private String dateEndService;
    private String comment;
    private String status;
    private String service;
    private String queue;
    private String contractor;
    private String ticketSellings;
    private String name_manager;


    public String getNameTalon() {
        return nameTalon;
    }

    public void setNameTalon(String nameTalon) {
        this.nameTalon = nameTalon;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDatePrinted() {
        return datePrinted;
    }

    public void setDatePrinted(String datePrinted) {
        this.datePrinted = datePrinted;
    }

    public String getDateDistrib() {
        return dateDistrib;
    }

    public void setDateDistrib(String dateDistrib) {
        this.dateDistrib = dateDistrib;
    }

    public String getDateStartService() {
        return dateStartService;
    }

    public void setDateStartService(String dateStartService) {
        this.dateStartService = dateStartService;
    }

    public String getDateEndService() {
        return dateEndService;
    }

    public void setDateEndService(String dateEndService) {
        this.dateEndService = dateEndService;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getTicketSellings() {
        return ticketSellings;
    }

    public void setTicketSellings(String ticketSellings) {
        this.ticketSellings = ticketSellings;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName_manager() {
        return name_manager;
    }

    public void setName_manager(String name_manager) {
        this.name_manager = name_manager;
    }
}
