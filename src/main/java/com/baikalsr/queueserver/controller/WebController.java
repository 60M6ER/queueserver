package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.editorImpl.TicketCreatorUI;
import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.repository.*;
import com.baikalsr.queueserver.service.CreatorTicket;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import com.baikalsr.queueserver.service.TicketDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private KioskRepo kioskRepo;
    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private TicketServiceRepo ticketServiceRepo;
    @Autowired
    private CreatorTicket creatorTicket;
    @Autowired
    private KioskMenuRepo kioskMenuRepo;
    @Autowired
    private TicketDistribution ticketDistribution;

    //Обеспечание шапки страниц
    @Autowired
    private MenuUI menuUI;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private StatusManager statusManager;

    @ModelAttribute("statusManager")
    public String getStatusManager() {
        return statusManager.statusToString(statusManager.getStatusManager(securityService.getUsername()));
    }

    @ModelAttribute("menuUI")
    public MenuUI getMenuUI() {
        return menuUI;
    }

    @ModelAttribute("nameUser")
    public String getUserName() {
        return securityService.getNameUser();
    }

    @ModelAttribute("securityService")
    public SecurityService getSecurityService() {
        return securityService;
    }
    //Конец обеспечения шапки страниц

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("ticketCreatorUI", new TicketCreatorUI());
        model.addAttribute("queues", queueRepo.findAll());
        model.addAttribute("services", ticketServiceRepo.findAll());
        model.addAttribute("messageCreate", "");
        model.addAttribute("tickets", ticketDistribution.getListTickets());
        return "index";
    }

    @RequestMapping("/settings")
    public String settings(Model model, @RequestParam(required = false) String currentSet){
        if (currentSet != null) {
            if (currentSet.equals("Users")) {
                List<Manager> managers = managerRepo.findAll();
                model.addAttribute("Users", managers);
                model.addAttribute("currentSet", currentSet);
            }
            if (currentSet.equals("Kiosks")) {
                List<Kiosk> kiosks = kioskRepo.findAll();
                model.addAttribute("Kiosks", kiosks);
                model.addAttribute("currentSet", currentSet);
            }
            if (currentSet.equals("KiosksMenu")) {
                List<KioskMenu> kioskMenus = kioskMenuRepo.findAll();
                model.addAttribute("KioskMenus", kioskMenus);
                model.addAttribute("currentSet", currentSet);
            }
            if (currentSet.equals("Queues")) {
                List<Queue> queues = queueRepo.findAll();
                model.addAttribute("queues", queues);
                model.addAttribute("currentSet", currentSet);
            }
            if (currentSet.equals("TicketService")) {
                List<TicketService> services = ticketServiceRepo.findAll();
                model.addAttribute("services", services);
                model.addAttribute("currentSet", currentSet);
            }
        }
        return "settings";
    }

    @RequestMapping(value = "/createTicket", params = {"create"}, method = RequestMethod.POST)
    public String createTicket(@ModelAttribute("ticketCreatorUI") TicketCreatorUI ticketCreatorUI, Model model){
        Ticket ticket = creatorTicket.createTicket(ticketCreatorUI.getService(), ticketCreatorUI.getQueue());

        model.addAttribute("ticketCreatorUI", new TicketCreatorUI());
        model.addAttribute("queues", queueRepo.findAll());
        model.addAttribute("services", ticketServiceRepo.findAll());
        model.addAttribute("messageCreate", "Созадн новый талон: " + ticket.getName());
        model.addAttribute("tickets", ticketDistribution.getListTickets());
        return "index";
    }
}
