package com.baikalsr.printservice.controller;

import com.baikalsr.printservice.entity.ListJobs;
import com.baikalsr.printservice.entity.PrintJob;
import com.baikalsr.printservice.entity.StatusesPrinter;
import com.baikalsr.printservice.service.QueuePrint;
import com.baikalsr.printservice.service.ServicePrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/printer")
public class PrinterRestController {
    @Autowired
    ServicePrint servicePrint;
    @Autowired
    QueuePrint queuePrint;


    @GetMapping("/get_statuses")
    private StatusesPrinter getStatuses() {

        StatusesPrinter statusesPrinter = new StatusesPrinter();
        statusesPrinter.setStatusPrinter(servicePrint.getStatusPrinter().name());
        statusesPrinter.setStatusCuter(servicePrint.getStatusCuter().name());
        statusesPrinter.setStatusHead(servicePrint.getStatusHead().name());
        statusesPrinter.setStatusPaper(servicePrint.getStatusPaper().name());

        return statusesPrinter;
    }

    @GetMapping("/get_jobs")
    private ListJobs getJobs() {

        ListJobs listJobs = new ListJobs();
        listJobs.setList(queuePrint.getPrintJobs());

        return listJobs;
    }

    @PostMapping(value = "/new_job", consumes = "application/json", produces = "application/json")
    private PrintJob newJob(@RequestBody PrintJob printJob) {

        PrintJob newPrintJob = queuePrint.addJob(printJob);

        return newPrintJob;
    }

    @GetMapping(value = "/get_status_job", params = {"id"})
    private PrintJob getStatusJob(HttpServletRequest req) {

        PrintJob newPrintJob = queuePrint.getJobByNumber(UUID.fromString(req.getParameter("id")));

        return newPrintJob;
    }
}
