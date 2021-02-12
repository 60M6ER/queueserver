package com.baikalsr.queueserver.jsonView.administration;

import com.baikalsr.queueserver.entity.StatusDevice;

import java.util.ArrayList;
import java.util.List;

public class StatusPageAdPOJO {
    private List<TicketAd> tickets;
    private List<ManagerAd> managers;
    private List<KioskPOJO> kiosks;
    private List<TabloPOJO> tablos;

    public StatusPageAdPOJO() {
        this.tickets = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.kiosks = new ArrayList<>();
        this.tablos = new ArrayList<>();
    }

    public void addTicket(TicketAd ticketAd) {
        tickets.add(ticketAd);
    }

    public void addManager(ManagerAd managerAd) {
        managers.add(managerAd);
    }

    public List<ManagerAd> getManagers() {
        return managers;
    }

    public void setManagers(List<ManagerAd> managers) {
        this.managers = managers;
    }

    public List<TicketAd> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketAd> tickets) {
        this.tickets = tickets;
    }

    public List<KioskPOJO> getKiosks() {
        return kiosks;
    }

    public void setKiosks(List<KioskPOJO> kiosks) {
        this.kiosks = kiosks;
    }

    public List<TabloPOJO> getTablos() {
        return tablos;
    }

    public void setTablos(List<TabloPOJO> tablos) {
        this.tablos = tablos;
    }

    public static class TicketAd{
        private String casement;
        private String ticket;
        private int status;
        private String statusName;
        private String manager;
        private String dateQueue;
        private String dateDistrib;
        private String dateService;

        public String getCasement() {
            return casement;
        }

        public void setCasement(String casement) {
            this.casement = casement;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public String getDateQueue() {
            return dateQueue;
        }

        public void setDateQueue(String dateQueue) {
            this.dateQueue = dateQueue;
        }

        public String getDateDistrib() {
            return dateDistrib;
        }

        public void setDateDistrib(String dateDistrib) {
            this.dateDistrib = dateDistrib;
        }

        public String getDateService() {
            return dateService;
        }

        public void setDateService(String dateService) {
            this.dateService = dateService;
        }
    }

    public static class ManagerAd{
        private String loginad;
        private String name;
        private String date;
        private String casement;
        private String status;
        private String statusName;
        private String time;

        public String getLoginad() {
            return loginad;
        }

        public void setLoginad(String loginad) {
            this.loginad = loginad;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCasement() {
            return casement;
        }

        public void setCasement(String casement) {
            this.casement = casement;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }
    }

    public static class KioskPOJO{
        private String name;
        private StatusDevice statusDevice;
        private String message;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public StatusDevice getStatusDevice() {
            return statusDevice;
        }

        public void setStatusDevice(StatusDevice statusDevice) {
            this.statusDevice = statusDevice;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class TabloPOJO {
        private String name;
        private StatusDevice statusDevice;
        private String message;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public StatusDevice getStatusDevice() {
            return statusDevice;
        }

        public void setStatusDevice(StatusDevice statusDevice) {
            this.statusDevice = statusDevice;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
