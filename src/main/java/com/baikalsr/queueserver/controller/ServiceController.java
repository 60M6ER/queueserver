package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.UISettings;
import com.baikalsr.queueserver.UI.editorImpl.QueueEdit;
import com.baikalsr.queueserver.UI.editorImpl.TicketServiceEdit;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.TicketService;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.repository.TicketServiceRepo;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ServiceController {

    @Autowired
    private QueueRepo queueRepo;

    @Autowired
    private TicketServiceEdit serviceEdit;

    @Autowired
    private TicketServiceRepo ticketServiceRepo;

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


    @RequestMapping("/settings/service")
    public String userSet(Model model, @RequestParam Long id){

        TicketServiceEdit newServiceEdit = serviceEdit.cloneObj();

        if (id == -1) //Создаем нового
            newServiceEdit.rebuildServiceEdit(new TicketService());
        else //Редактируем существующего
            newServiceEdit.rebuildServiceEdit(ticketServiceRepo.getOne(id));

        UISettings.poolEditObjects.put(newServiceEdit.getUUID(), newServiceEdit);
        model.addAttribute("service", newServiceEdit);
        model.addAttribute("errorPass", "");

        return "service";
    }

    @RequestMapping(value = "/saveService", params = {"addManager"}, method = RequestMethod.POST)
    public String addRoleUser(@ModelAttribute("service") TicketServiceEdit serviceEdit, HttpServletRequest req, Model model){
        TicketServiceEdit serviceEditPool = (TicketServiceEdit) UISettings.poolEditObjects.get(serviceEdit.getUUID());
        if (serviceEdit.getAddingManager() != null) {
            serviceEditPool.getManagers().add(serviceEdit.getAddingManager());
        }
        serviceEdit.updateServiceEdit(serviceEditPool);

        model.addAttribute("service", serviceEdit);
        model.addAttribute("errorPass", "");
        return "/service";
    }

    @RequestMapping(value = "/saveService", params = {"delManager"}, method = RequestMethod.POST)
    public String delRoleUser(@ModelAttribute("service") TicketServiceEdit serviceEdit, HttpServletRequest req, Model model){
        TicketServiceEdit serviceEditPool = (TicketServiceEdit) UISettings.poolEditObjects.get(serviceEdit.getUUID());
        serviceEditPool.getManagers().remove(Integer.parseInt(req.getParameter("delManager")));
        serviceEdit.updateServiceEdit(serviceEditPool);
        model.addAttribute("service", serviceEdit);
        model.addAttribute("errorPass", "");
        return "/service";
    }

    @RequestMapping(value = "/saveService", params = {"save"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("service") TicketServiceEdit serviceEdit, Model model){
        TicketServiceEdit serviceEditPool = (TicketServiceEdit) UISettings.poolEditObjects.get(serviceEdit.getUUID());
        serviceEdit.updateServiceEdit(serviceEditPool);
        model.addAttribute("service", serviceEdit);

        TicketService ticketService = new TicketService(serviceEdit);
        ticketServiceRepo.save(ticketService);
        UISettings.poolEditObjects.remove(serviceEdit.getUUID());
        return "/settings";
    }
}
