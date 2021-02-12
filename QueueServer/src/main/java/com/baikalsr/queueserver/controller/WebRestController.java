package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.TicketService;
import com.baikalsr.queueserver.exceptions.ManagerNotFound;
import com.baikalsr.queueserver.jsonView.ServicesPOJO;
import com.baikalsr.queueserver.jsonView.administration.QueuesPOJO;
import com.baikalsr.queueserver.jsonView.tablo.TabloSettingsPOJO;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.TicketServiceRepo;
import com.baikalsr.queueserver.service.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class WebRestController {

    @Autowired
    private TicketServiceRepo serviceRepo;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private ManagerRepo managerRepo;

    @GetMapping(value = "/get_services")
    private ArrayList<ServicesPOJO> get_services(HttpServletRequest req) {
        List<TicketService> services = serviceRepo.getAllOrderByName();
        ArrayList<ServicesPOJO> pojoArrayList = new ArrayList<>();
        for (int i = 0; i < services.size(); i++) {
            pojoArrayList.add(new ServicesPOJO(services.get(i)));
        }
        return pojoArrayList;
    }

    @GetMapping(value = "/get_queues", params = {"user_id"})
    private QueuesPOJO get_queues(HttpServletRequest req) {
        String loginAD = req.getParameter("user_id");
        Manager manager = managerRepo.findFirstByLoginAD(loginAD);
        if (manager == null)
            throw new ManagerNotFound(loginAD);

        return administrationService.getQueues(manager, null, true);
    }
}
