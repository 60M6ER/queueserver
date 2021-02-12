package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.jsonView.reports.ReportTicketListPOJO;
import com.baikalsr.queueserver.jsonView.reports.ReportTicketNormPOJO;
import com.baikalsr.queueserver.jsonView.reports.ToFormRequestPOJO;
import com.baikalsr.queueserver.jsonView.reports.ToFormTicketListPOJO;
import com.baikalsr.queueserver.service.reports.ReportsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/rest/reports")
public class ReportsRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsRestController.class);
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU"));
    private SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));

    @Autowired
    private ReportsService reportsService;

    @PostMapping(value = "/toformreport", consumes = "application/json", produces = "application/json")
    private IdReportPOJO toFormReport(@RequestBody ToFormRequestPOJO toFormRequestPOJO) {
        try {
            return new IdReportPOJO(reportsService.toFormReport(toFormRequestPOJO));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/getResultReport", params = {"id"}, method = RequestMethod.GET)
    private ReportTicketNormPOJO getResultReport(HttpServletRequest req) {
        UUID uuid = UUID.fromString(req.getParameter("id"));
        return (ReportTicketNormPOJO) reportsService.getResult(uuid);
    }

    @PostMapping(value = "/toformticketlist", consumes = "application/json", produces = "application/json")
    private IdReportPOJO toformticketlist(@RequestBody ToFormTicketListPOJO toFormTicketListPOJO) {
        try {
            return new IdReportPOJO(reportsService.toFormTicketList(toFormTicketListPOJO));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/getResultTicketList", params = {"id"}, method = RequestMethod.GET)
    private ReportTicketListPOJO getResultTicketList(HttpServletRequest req) {
        UUID uuid = UUID.fromString(req.getParameter("id"));
        return (ReportTicketListPOJO) reportsService.getResult(uuid);
    }

    private class IdReportPOJO{
        private UUID id;

        public IdReportPOJO() {
        }

        public IdReportPOJO(UUID id) {
            this.id = id;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }
    }
}
