package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.IMPL.KioskServiceIMPL;
import com.baikalsr.queueserver.UI.KioskUI;
import com.baikalsr.queueserver.jsonVeiw.DateTime;
import com.baikalsr.queueserver.jsonVeiw.ServiceList;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
@RequestMapping("/kioskt")
public class KioskRestController {
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU"));
    private SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));

    @Autowired
    private KioskServiceIMPL kioskService;

    @GetMapping("/get_time")
    private DateTime getDateTime() {

        Date dateTime = new Date();
        DateTime dateTimeRes = new DateTime();
        dateTimeRes.setDate(formatDate.format(dateTime));
        dateTimeRes.setTime(formatTime.format(dateTime));

        return dateTimeRes;
    }

    @GetMapping("get_services")
    private ServiceList getServices(HttpServletRequest req) {
        return kioskService.getServices(req.getRemoteAddr());
    }


    @GetMapping(value = "/clickservice", params = {"id"})
    private ServiceList clickSubmit(HttpServletRequest req) {
        return kioskService.getServices(req.getRemoteAddr(), Long.parseLong(req.getParameter("id")));
    }

    @GetMapping(value = "/clickservice", params = {"id", "yesno"})
    private ServiceList clickYesNoSubmit(HttpServletRequest req) {
        return kioskService.getServices(req.getRemoteAddr(), Long.parseLong(req.getParameter("id")), req.getParameter("yesno"));
    }
}
