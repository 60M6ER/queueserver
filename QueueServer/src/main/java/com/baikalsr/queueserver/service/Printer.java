package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.PrintJob;
import com.baikalsr.queueserver.entity.StatusPrinter;
import com.baikalsr.queueserver.jsonView.StatusesPrinterView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.UUID;

public class Printer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Printer.class);

    private final String uriGetStatusJob = "/printer/get_status_job";
    private final String uriNewJob = "/printer/new_job";
    private final String uriGetStatuses = "/printer/get_statuses";

    private String URL;
    private boolean working;
    private StatusPrinter statusPrinter;
    private StatusPrinter statusPaper;
    private StatusPrinter statusCuter;
    private StatusPrinter statusHead;

    public Printer(String URL) {
        this.URL = URL;
        updateStatuses();
    }

    public void updateStatuses() {
        working = false;
        RestTemplate restTemplate = new RestTemplate();
        String json = "";
        try {
            json = restTemplate.getForObject(URL + uriGetStatuses, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            StatusesPrinterView statusesPrinterView = objectMapper.readValue(json, StatusesPrinterView.class);
            statusPrinter = statusesPrinterView.getStatusPrinter();
            statusPaper = statusesPrinterView.getStatusPaper();
            statusCuter = statusesPrinterView.getStatusCuter();
            statusHead = statusesPrinterView.getStatusHead();
            working = statusPrinter == StatusPrinter.ONLINE && statusPaper != StatusPrinter.NO_PAPER &&
                    statusHead == StatusPrinter.PRINT_HEAD_NORMAL && statusCuter == StatusPrinter.CUTTER_NORMAL;
        } catch (HttpServerErrorException | ResourceAccessException e ) {
            LOGGER.error("Не удалось связаться с сервисом печати киоска: " + URL);
            e.printStackTrace();
        } catch (JsonMappingException e) {
            LOGGER.error("Ошибка разбора JSON");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            LOGGER.error("Ошибка разбора JSON");
            e.printStackTrace();
        }
    }

    public PrintJob newJob(PrintJob printJob) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PrintJob> requestBody = new HttpEntity<PrintJob>(printJob, headers);
        ResponseEntity<PrintJob> result = restTemplate.postForEntity(URL + uriNewJob, requestBody, PrintJob.class);
        if (result.getStatusCode() == HttpStatus.OK)
            return result.getBody();
        return null;
    }

    public PrintJob getStatusJob(UUID id) {
        RestTemplate restTemplate = new RestTemplate();
        String parameters = "id=" + id;
        ResponseEntity<PrintJob> result = restTemplate.getForEntity(URL + uriGetStatusJob + "?" + parameters, PrintJob.class);
        if (result.getStatusCode() == HttpStatus.OK)
            return result.getBody();
        return null;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

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
