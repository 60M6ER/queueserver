package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.exceptions.ManagerNotFound;
import com.baikalsr.queueserver.exceptions.QueueNotFound;
import com.baikalsr.queueserver.jsonView.DateTime;
import com.baikalsr.queueserver.jsonView.administration.*;
import com.baikalsr.queueserver.jsonView.workStation.StatusEmployeePOJO;
import com.baikalsr.queueserver.jsonView.workStation.StatusesEmployeePOJO;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.service.AdministrationService;
import com.baikalsr.queueserver.service.WorkStationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/rest/administration_page")
public class AdministrationRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationRestController.class);
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU"));
    private SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));

    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private WorkStationManager workStationManager;

    @GetMapping("/get_time")
    private DateTime getDateTime() {

        Date dateTime = new Date();
        DateTime dateTimeRes = new DateTime();
        dateTimeRes.setDate(formatDate.format(dateTime));
        dateTimeRes.setTime(formatTime.format(dateTime));

        return dateTimeRes;
    }

    @GetMapping(value = "/get_queues", params = {"user_id"})
    private QueuesPOJO get_queues(HttpServletRequest req) {
        String loginAD = req.getParameter("user_id");
        Manager manager = managerRepo.findFirstByLoginAD(loginAD.toLowerCase());
        if (manager == null)
            throw new ManagerNotFound(loginAD);

        return administrationService.getQueues(manager, null, false);
    }

    @GetMapping(value = "/get_status_page", params = {"queue_id"})
    private StatusPageAdPOJO get_status_page(HttpServletRequest req) {
        Long queueId = Long.parseLong(req.getParameter("queue_id"));
        Queue queue = queueRepo.getOne(queueId);
        if (queue == null) {
            throw new QueueNotFound(queueId);
        }

        return administrationService.getTicketList(queue);
    }

    @GetMapping(value = "/get_lists_managers", params = {"queue_id"})
    private ManagersListsPOJO get_lists_managers(HttpServletRequest req) {
        Long queueId = Long.parseLong(req.getParameter("queue_id"));
        Queue queue = queueRepo.getOne(queueId);
        if (queue == null) {
            throw new QueueNotFound(queueId);
        }

        return administrationService.getListsManagers(queue);
    }

    @GetMapping(value = "/get_manager_data", params = {"manager_id", "currentManager"})
    private ManagerDataPOJO get_manager_data(HttpServletRequest req) {
        Long managerId = Long.parseLong(req.getParameter("manager_id"));
        Manager manager = managerRepo.getOne(managerId);
        if (manager == null) {
            throw new ManagerNotFound(managerId);
        }
        Manager curManager = managerRepo.findFirstByLoginAD(req.getParameter("currentManager").toLowerCase());
        if (manager == null) {
            throw new ManagerNotFound(managerId);
        }

        return administrationService.getManagerData(manager, curManager);
    }

    @GetMapping(value = "/get_queue_data", params = {"queue_id"})
    private QueueDataPOJO get_queue_data(HttpServletRequest req) {
        Long queueId = Long.parseLong(req.getParameter("queue_id"));
        Queue queue = queueRepo.getOne(queueId);
        if (queue == null) {
            throw new QueueNotFound(queueId);
        }

        return administrationService.getQueueData(queue);
    }

    @PostMapping(value = "/saveManagersData", consumes = "application/json", produces = "application/json")
    private String saveManagersData(@RequestBody SaveManagersPOJO saveManagersPOJO) {

        try {
            administrationService.saveDataManager(saveManagersPOJO);

            return "ok";
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/saveQueueData", consumes = "application/json", produces = "application/json")
    private String saveQueueData(@RequestBody QueueDataPOJO queueDataPOJO) {

        try {
            administrationService.saveDataQueue(queueDataPOJO);

            return "ok";
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
