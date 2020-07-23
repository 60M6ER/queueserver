package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.WorkStationUI;
import com.baikalsr.queueserver.UI.editorImpl.TicketServiceEdit;
import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import com.baikalsr.queueserver.service.WorkStationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WorkStationController {

    @Autowired
    private WorkStationManager workStationManager;

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

    @GetMapping("/workStation")
    public String workStation(Model model) {
        model.addAttribute("workStationUI", workStationManager.getWorkStationUI(securityService.getUsername()));
        return "workStation";
    }

    @RequestMapping(value = "/WorkSession", params = {"startWorkSession"}, method = RequestMethod.POST)
    public String startWorkSession(@ModelAttribute("workStationUI") WorkStationUI workStationUI, HttpServletRequest req, Model model) {
        workStationUI = workStationManager.updateWorkStationUI(workStationUI);
        workStationUI.setWorking(Integer.parseInt(req.getParameter("startWorkSession")) == 1);
        if (workStationUI.isWorking()) {
            workStationManager.startSession(workStationUI.getCasement(), workStationUI.getLoginAD());
        }
        else
            workStationManager.endSession(workStationUI.getLoginAD());
        model.addAttribute("workStationUI", workStationUI);
        return "workStation";
    }

    @RequestMapping(value = "/WorkSession", params = {"serviceClient"}, method = RequestMethod.POST)
    public String serviceClient(@ModelAttribute("workStationUI") WorkStationUI workStationUI, HttpServletRequest req, Model model) {
        workStationUI = workStationManager.updateWorkStationUI(workStationUI);
        int param = Integer.parseInt(req.getParameter("serviceClient"));
        if (param == 1) {
            workStationManager.serviceClient(workStationUI.getLoginAD());
        }
        else
            workStationManager.endServiceClient(workStationUI.getLoginAD());
        model.addAttribute("workStationUI", workStationUI);
        return "workStation";
    }
}
