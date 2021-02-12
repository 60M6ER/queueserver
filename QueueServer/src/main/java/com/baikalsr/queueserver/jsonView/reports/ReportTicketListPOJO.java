package com.baikalsr.queueserver.jsonView.reports;

import java.util.ArrayList;
import java.util.List;

public class ReportTicketListPOJO {

    private int countPages;
    private int currentPage;
    private List<TicketLinePOJO> linesPOJO;

    public ReportTicketListPOJO() {
        this.linesPOJO = new ArrayList<>();
    }

    public List<TicketLinePOJO> getLinesPOJO() {
        return linesPOJO;
    }

    public void setLinesPOJO(List<TicketLinePOJO> linesPOJO) {
        this.linesPOJO = linesPOJO;
    }

    public int getCountPages() {
        return countPages;
    }

    public void setCountPages(int countPages) {
        this.countPages = countPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public static class TicketLinePOJO{
        private String nameTalonField;
        private String nameClientField;
        private String dateCreateField;
        private String datePrintedField;
        private String dateDistribField;
        private String dateStartServiceField;
        private String dateEndServiceField;
        private String commentField;
        private String statusField;
        private String serviceField;
        private String queueField;
        private String contractorField;
        private String ticketSellingsField;
        private String name_managerField;

        public String getNameTalonField() {
            return nameTalonField;
        }

        public void setNameTalonField(String nameTalonField) {
            this.nameTalonField = nameTalonField;
        }

        public String getNameClientField() {
            return nameClientField;
        }

        public void setNameClientField(String nameClientField) {
            this.nameClientField = nameClientField;
        }

        public String getDateCreateField() {
            return dateCreateField;
        }

        public void setDateCreateField(String dateCreateField) {
            this.dateCreateField = dateCreateField;
        }

        public String getDatePrintedField() {
            return datePrintedField;
        }

        public void setDatePrintedField(String datePrintedField) {
            this.datePrintedField = datePrintedField;
        }

        public String getDateDistribField() {
            return dateDistribField;
        }

        public void setDateDistribField(String dateDistribField) {
            this.dateDistribField = dateDistribField;
        }

        public String getDateStartServiceField() {
            return dateStartServiceField;
        }

        public void setDateStartServiceField(String dateStartServiceField) {
            this.dateStartServiceField = dateStartServiceField;
        }

        public String getDateEndServiceField() {
            return dateEndServiceField;
        }

        public void setDateEndServiceField(String dateEndServiceField) {
            this.dateEndServiceField = dateEndServiceField;
        }

        public String getCommentField() {
            return commentField;
        }

        public void setCommentField(String commentField) {
            this.commentField = commentField;
        }

        public String getStatusField() {
            return statusField;
        }

        public void setStatusField(String statusField) {
            this.statusField = statusField;
        }

        public String getServiceField() {
            return serviceField;
        }

        public void setServiceField(String serviceField) {
            this.serviceField = serviceField;
        }

        public String getQueueField() {
            return queueField;
        }

        public void setQueueField(String queueField) {
            this.queueField = queueField;
        }

        public String getContractorField() {
            return contractorField;
        }

        public void setContractorField(String contractorField) {
            this.contractorField = contractorField;
        }

        public String getName_managerField() {
            return name_managerField;
        }

        public void setName_managerField(String name_managerField) {
            this.name_managerField = name_managerField;
        }

        public String getTicketSellingsField() {
            return ticketSellingsField;
        }

        public void setTicketSellingsField(String ticketSellingsField) {
            this.ticketSellingsField = ticketSellingsField;
        }
    }
}
