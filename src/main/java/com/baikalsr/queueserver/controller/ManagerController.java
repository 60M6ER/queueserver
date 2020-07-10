package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManagerController {
    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private QueueRepo queueRepo;

    private Manager manager;

    @RequestMapping("/settings/user")
    public String userSet(Model model, @RequestParam Long id){

        manager = managerRepo.getOne(id);
        model.addAttribute("manager", manager);
        model.addAttribute("queues", queueRepo.findAll());

        return "user";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public String userSet(Model model, @ModelAttribute Manager manager){
        return "/settings";
    }
}
