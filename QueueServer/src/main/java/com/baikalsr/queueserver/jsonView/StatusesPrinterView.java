package com.baikalsr.queueserver.jsonView;

import com.baikalsr.queueserver.entity.StatusPrinter;

public class StatusesPrinterView {
    private StatusPrinter statusPrinter;
    private StatusPrinter statusPaper;
    private StatusPrinter statusCuter;
    private StatusPrinter statusHead;

    public StatusPrinter getStatusPrinter() {
        return statusPrinter;
    }

    public void setStatusPrinter(StatusPrinter statusPrinter) {
        this.statusPrinter = statusPrinter;
    }

    public StatusPrinter getStatusPaper() {
        return statusPaper;
    }

    public void setStatusPaper(StatusPrinter statusPaper) {
        this.statusPaper = statusPaper;
    }

    public StatusPrinter getStatusCuter() {
        return statusCuter;
    }

    public void setStatusCuter(StatusPrinter statusCuter) {
        this.statusCuter = statusCuter;
    }

    public StatusPrinter getStatusHead() {
        return statusHead;
    }

    public void setStatusHead(StatusPrinter statusHead) {
        this.statusHead = statusHead;
    }
}
