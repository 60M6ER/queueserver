package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.WorkStationUI;

import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import com.baikalsr.queueserver.service.WorkStationUIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class WorkStationController {

    @Autowired
    private WorkStationUIService workStationUIService;

    //Обеспечание шапки страниц
    @Autowired
    private MenuUI menuUI;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private StatusManager statusManager;

    @ModelAttribute("menuUI")
    public ArrayList<HashMap<String, Object>> getMenuUI() {
        return menuUI.getMenuStructByRoles();
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

    @GetMapping("/workStation")
    public String workStation(Model model) {
        //model.addAttribute("workStationUI", workStationUIService.getWorkStationUI());
        return "workStation";
    }

    @RequestMapping(value = "/WorkSession", params = {"startWorkSession"}, method = RequestMethod.POST)
    public String startWorkSession(@ModelAttribute("workStationUI") WorkStationUI workStationUI, HttpServletRequest req, Model model) {
        String error = "";
        boolean startSession = Integer.parseInt(req.getParameter("startWorkSession")) == 1;
        if (startSession) {
            error = workStationUIService.startSession(workStationUI);
        }
        else
            error = workStationUIService.endSession();
        int casement = workStationUI.getCasement();
        workStationUI = workStationUIService.getWorkStationUI();
        if (error != "" && startSession)
            workStationUI.setCasement(casement);
        workStationUI.setErrorEditSession(error);
        model.addAttribute("workStationUI", workStationUI);
        return "workStation";
    }

    @RequestMapping(value = "/WorkSession", params = {"serviceClient"}, method = RequestMethod.POST)
    public String serviceClient(@ModelAttribute("workStationUI") WorkStationUI workStationUI, HttpServletRequest req, Model model) {
        String error = "";
        boolean startServicing = Integer.parseInt(req.getParameter("serviceClient")) == 1;
        if (startServicing) {
            error = workStationUIService.startServicing(workStationUI);
        }
        else
            error = workStationUIService.endServicing(workStationUI);
        workStationUI = workStationUIService.getWorkStationUI();
        if (error != "") workStationUI.setErrorEditSession(error);

        model.addAttribute("workStationUI", workStationUI);
        return "workStation";
    }
}
