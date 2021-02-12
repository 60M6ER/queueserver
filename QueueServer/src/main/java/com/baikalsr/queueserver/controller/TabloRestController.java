package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.jsonView.tablo.RunLinesPOJO;
import com.baikalsr.queueserver.jsonView.tablo.TabloSettingsPOJO;
import com.baikalsr.queueserver.jsonView.tablo.TicketsListForTabloPOJO;
import com.baikalsr.queueserver.service.TabloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/tablo")
public class TabloRestController {

    @Autowired
    private TabloService tabloService;

    @GetMapping(value = "/initTablo")
    private TabloSettingsPOJO initTablo(HttpServletRequest req) {
        return tabloService.initTablo(req.getRemoteAddr());
    }

    @GetMapping(value = "/getListTickets")
    private TicketsListForTabloPOJO getListTickets(HttpServletRequest req) {
        return tabloService.getTicketsList(req.getRemoteAddr());
    }

    @GetMapping(value = "/getRunLines")
    private RunLinesPOJO getRunLines(HttpServletRequest req) {
        return tabloService.getRunLinesList(req.getRemoteAddr());
    }
}
