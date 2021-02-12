package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.jsonView.workStation.*;
import com.baikalsr.queueserver.jsonView.ResponseStatus;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.TicketRepo;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import com.baikalsr.queueserver.service.WorkStationManager;
import com.baikalsr.queueserver.service.WorkStationUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.UUID;

@RestController
@RequestMapping("/rest/work_station")
public class WorkStationRestController {

    @Autowired
    private WorkStationUIService workStationUIService;
    @Autowired
    private WorkStationManager workStationManager;
    @Autowired
    private TicketRepo ticketRepo;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkStationRestController.class);

    //Обеспечание шапки страниц
    @Autowired
    private MenuUI menuUI;
    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private StatusManager statusManager;

    @ModelAttribute("menuUI")
    public MenuUI getMenuUI() {
        return menuUI;
    }

    @ModelAttribute("nameUser")
    public String getUserName() {
        return securityService.getNameUser();
    }

    @ModelAttribute("userID")
    public String getUserID() {
        return securityService.getUsername();
    }

    @ModelAttribute("securityService")
    public SecurityService getSecurityService() {
        return securityService;
    }
    //Конец обеспечения шапки страниц
    /////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //Методы GET
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/getElQueue", params = {"idUser"}, method = RequestMethod.GET)
    private ElQueuePOJO getElQueue(HttpServletRequest req) {
        try {
            Manager manager = managerRepo.findFirstByLoginAD(req.getParameter("idUser").toLowerCase());
            if (manager == null)
                throw getWrongUser(req.getParameter("idUser"));

            ElQueuePOJO elQueuePOJO = new ElQueuePOJO();
            elQueuePOJO.setName(manager.getQueue().getName());
            elQueuePOJO.setClientsInQueue(ticketRepo.getCountClientsInQueue(manager.getQueue().getId()));
            return elQueuePOJO;
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/getStatuses", params = {"idUser"}, method = RequestMethod.GET)
    private StatusesEmployeePOJO getStatuses(HttpServletRequest req) {
        try {
            Manager manager = managerRepo.findFirstByLoginAD(req.getParameter("idUser").toLowerCase());
            if (manager == null)
                throw getWrongUser(req.getParameter("idUser"));

            return workStationManager.getStatusesManager(manager, manager, true);
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/getStatus", params = {"idUser"}, method = RequestMethod.GET)
    private StatusEmployeePOJO getStatus(HttpServletRequest req) {
        try {
            Manager manager = managerRepo.findFirstByLoginAD(req.getParameter("idUser").toLowerCase());
            if (manager == null)
                throw getWrongUser(req.getParameter("idUser"));

            return new StatusEmployeePOJO(statusManager.getStatusManager(manager));
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/isTicket", params = {"idUser"}, method = RequestMethod.GET)
    private TicketStatusPOJO isTicket(HttpServletRequest req) {
        try {
            Manager manager = managerRepo.findFirstByLoginAD(req.getParameter("idUser").toLowerCase());
            if (manager == null)
                throw getWrongUser(req.getParameter("idUser"));

            TicketStatusPOJO ticketStatusPOJO = new TicketStatusPOJO();
            Ticket ticket = workStationManager.getServicingTicket(manager);
            ticketStatusPOJO.setTicket(ticket != null);
            ticketStatusPOJO.setId(ticket != null ? ticket.getId() : null);


            return ticketStatusPOJO;
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/getTicketsPaused", params = {"idUser"}, method = RequestMethod.GET)
    private ListTalonPOJO getTicketsPaused(HttpServletRequest req) {
        try {
            Manager manager = managerRepo.findFirstByLoginAD(req.getParameter("idUser").toLowerCase());
            if (manager == null)
                throw getWrongUser(req.getParameter("idUser"));

            return workStationManager.getTicketsPaused(manager);
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/getTicket", params = {"idTicket"}, method = RequestMethod.GET)
    private TalonInfoPOJO getTicket(HttpServletRequest req) {
        try {

            return workStationManager.getTicket(UUID.fromString(req.getParameter("idTicket")));
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //Методы POST
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "/setNewStatus", consumes = "application/json", produces = "application/json")
    private ResponseStatus setNewStatus(@RequestBody SetNewStatusPOJO setNewStatus) {

        try {
            Manager manager = managerRepo.findFirstByLoginAD(setNewStatus.getIdUser().toLowerCase());
            if (manager == null)
                throw getWrongUser(setNewStatus.getIdUser());

            workStationManager.setNewStatus(manager, setNewStatus);

            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/startServiceClient", consumes = "application/json", produces = "application/json")
    private ResponseStatus startServiceClient(@RequestBody UserIDPojo userIDPojo) {

        try {
            Manager manager = managerRepo.findFirstByLoginAD(userIDPojo.getIdUser().toLowerCase());
            if (manager == null)
                throw getWrongUser(userIDPojo.getIdUser());

            workStationManager.startServiceClient(manager);

            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/endServiceClient", consumes = "application/json", produces = "application/json")
    private ResponseStatus endServiceClient(@RequestBody UserIDPojo userIDPojo) {

        try {
            Manager manager = managerRepo.findFirstByLoginAD(userIDPojo.getIdUser().toLowerCase());
            if (manager == null)
                throw getWrongUser(userIDPojo.getIdUser());

            workStationManager.endServiceClient(manager);

            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/pauseServiceTicket", consumes = "application/json", produces = "application/json")
    private ResponseStatus pauseServiceTicket(@RequestBody UserIDPojo userIDPojo) {

        try {
            Manager manager = managerRepo.findFirstByLoginAD(userIDPojo.getIdUser().toLowerCase());
            if (manager == null)
                throw getWrongUser(userIDPojo.getIdUser());

            workStationManager.pausedTicket(manager);

            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/resumeServiceTicket", consumes = "application/json", produces = "application/json")
    private ResponseStatus resumeServiceTicket(@RequestBody PauseTicketPojo pauseTicketPojo) {

        try {
            Manager manager = managerRepo.findFirstByLoginAD(pauseTicketPojo.getIdUser().toLowerCase());
            if (manager == null)
                throw getWrongUser(pauseTicketPojo.getIdUser());

            Ticket ticket = ticketRepo.getOne(pauseTicketPojo.getIdTicket());
            if (ticket == null)
                throw new RuntimeException("Талон не найден.");

            workStationManager.resumeServicingTicket(manager, ticket);

            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/createTicketPartner", consumes = "application/json", produces = "application/json")
    private StatusNewTalonPOJO createTicketPartner(@RequestBody UserIDPojo userIDPojo) {

        try {
            Manager manager = managerRepo.findFirstByLoginAD(userIDPojo.getIdUser().toLowerCase());
            if (manager == null)
                throw getWrongUser(userIDPojo.getIdUser());


            StatusNewTalonPOJO statusNewTalonPOJO = workStationManager.createTicketPartner(manager);

            return statusNewTalonPOJO;
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/setContractor", consumes = "application/json", produces = "application/json")
    private ResponseStatus setContractor(@RequestBody SetContractorPOJO contractorPOJO) {

        try {
            Ticket ticket = ticketRepo.getOne(contractorPOJO.getIdTicket());
            if (ticket == null)
                throw new RuntimeException("Талон не найден.");

            workStationManager.setContractorTicket(ticket, contractorPOJO);

            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/addSelling", consumes = "application/json", produces = "application/json")
    private ResponseStatus addSelling(@RequestBody AddSellingPojo addSellingPojo) {

        try {
            Ticket ticket = ticketRepo.getOne(addSellingPojo.getIdTicket());
            if (ticket == null)
                throw new RuntimeException("Талон не найден.");


            workStationManager.addSellingTicket(ticket, addSellingPojo);

            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }


    private RuntimeException getWrongUser(String parameter) {
        return new RuntimeException(String.format("Плохой \"userID\" (%s). Не найден менеджер с таким логином. Обратитест в техподдержку.", parameter));
    }
}
