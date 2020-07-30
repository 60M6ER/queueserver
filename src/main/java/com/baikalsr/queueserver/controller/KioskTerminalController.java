package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.IMPL.KioskServiceIMPL;
import com.baikalsr.queueserver.UI.KioskUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class KioskTerminalController {
    @Autowired
    private KioskServiceIMPL kioskService;

    @RequestMapping("/kiosk")
    public String kioskStart(HttpServletRequest req, Model model){
        KioskUI kioskUI = kioskService.getKioskUI(req.getRemoteAddr());

        model.addAttribute("kioskUI", kioskUI);
        return "kiosk";
    }

    @RequestMapping(value = "/setCommentKiosk", method = RequestMethod.POST)
    public String kioskStart(@ModelAttribute("kioskUI") KioskUI kioskUI, HttpServletRequest req, Model model){
        kioskService.setCommentKiosk(kioskUI.getComment(), req.getRemoteAddr());
//        kioskUI = kioskService.getKioskUI(req.getRemoteAddr());
//
//        model.addAttribute("kioskUI", kioskUI);
        return "/kiosk";
    }

}
