package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.IMPL.KioskServiceIMPL;
import com.baikalsr.queueserver.jsonView.DateTime;
import com.baikalsr.queueserver.jsonView.StatusJobPrinted;
import com.baikalsr.queueserver.jsonView.ServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

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

    @RequestMapping(value = "/isPrinted", params = {"id"}, method = RequestMethod.GET)
    private @ResponseBody String isPrinted(HttpServletRequest req) {
        StatusJobPrinted statusJobPrinted = kioskService.isPrinted(req.getRemoteAddr(), UUID.fromString(req.getParameter("id")));
        return "{\"printed\":\"" + statusJobPrinted.isPrinted() + "\"}";
    }
}
