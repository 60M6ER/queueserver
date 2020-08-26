package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.jsonView.StatusesPrinterView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Arrays;

public class Printer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Printer.class);

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
            json = restTemplate.getForObject(URL + "/printer/get_statuses", String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            StatusesPrinterView statusesPrinterView = objectMapper.readValue(json, StatusesPrinterView.class);
            statusPrinter = statusesPrinterView.getStatusPrinter();
            statusPaper = statusesPrinterView.getStatusPaper();
            statusCuter = statusesPrinterView.getStatusCuter();
            statusHead = statusesPrinterView.getStatusHead();
            working = statusPrinter == StatusPrinter.ONLINE && statusPaper != StatusPrinter.NO_PAPER &&
                    statusHead == StatusPrinter.PRINT_HEAD_NORMAL && statusCuter == StatusPrinter.CUTTER_NORMAL;
        } catch (HttpServerErrorException e) {
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
        ResponseEntity<PrintJob> result = restTemplate.postForEntity(URL + "/printer/new_job", requestBody, PrintJob.class);
        return result.getBody();
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
