package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class KioskTerminalController {
    @Autowired
    private KioskService kioskService;

    @RequestMapping("/kiosk")
    public String kioskStart(HttpServletRequest req, Model model){
        System.out.println(req.getRemoteAddr());
        return "kiosk";
    }

}
