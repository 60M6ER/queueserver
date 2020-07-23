package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.UISettings;
import com.baikalsr.queueserver.UI.editorImpl.QueueEdit;
import com.baikalsr.queueserver.UI.editorImpl.UserEdit;
import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.repository.TicketServiceRepo;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class QueueController {

    @Autowired
    private QueueRepo queueRepo;

    @Autowired
    private QueueEdit queueEdit;

    @Autowired
    private KioskRepo kioskRepo;

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

    @RequestMapping("/settings/queue")
    public String userSet(Model model, @RequestParam Long id){

        QueueEdit newQueueEdit = queueEdit.cloneObj();

        if (id == -1) //Создаем нового
            newQueueEdit.rebuildQueueEdit(new Queue());
        else //Редактируем существующего
            newQueueEdit.rebuildQueueEdit(queueRepo.getOne(id));

        UISettings.poolEditObjects.put(newQueueEdit.getUUID(), newQueueEdit);
        model.addAttribute("queue", newQueueEdit);
        model.addAttribute("kiosks", kioskRepo.findAll());
        model.addAttribute("errorPass", "");

        return "queue";
    }

    @RequestMapping(value = "/saveQueue", params = {"addManager"}, method = RequestMethod.POST)
    public String addRoleUser(@ModelAttribute("queue") QueueEdit queueEdit, HttpServletRequest req, Model model){
        QueueEdit queueEditPool = (QueueEdit) UISettings.poolEditObjects.get(queueEdit.getUUID());
        if (queueEdit.getAddingManager() != null) {
            queueEditPool.getManagers().add(queueEdit.getAddingManager());
        }
        queueEdit.updateQueueEdit(queueEditPool);

        model.addAttribute("queue", queueEdit);
        model.addAttribute("kiosks", kioskRepo.findAll());
        model.addAttribute("errorPass", "");
        return "/queue";
    }

    @RequestMapping(value = "/saveQueue", params = {"delManager"}, method = RequestMethod.POST)
    public String delRoleUser(@ModelAttribute("queue") QueueEdit queueEdit, HttpServletRequest req, Model model){
        QueueEdit queueEditPool = (QueueEdit) UISettings.poolEditObjects.get(queueEdit.getUUID());
        queueEditPool.getManagers().remove(Integer.parseInt(req.getParameter("delManager")));
        queueEdit.updateQueueEdit(queueEditPool);
        model.addAttribute("queue", queueEdit);
        model.addAttribute("kiosks", kioskRepo.findAll());
        model.addAttribute("errorPass", "");
        return "/queue";
    }

    @RequestMapping(value = "/saveQueue", params = {"save"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("queue") QueueEdit queueEdit, Model model){
        QueueEdit queueEditPool = (QueueEdit) UISettings.poolEditObjects.get(queueEdit.getUUID());
        queueEdit.updateQueueEdit(queueEditPool);
        model.addAttribute("queue", queueEdit);
        model.addAttribute("kiosks", kioskRepo.findAll());

        Queue queue = new Queue(queueEdit);
        queueRepo.save(queue);
        UISettings.poolEditObjects.remove(queueEdit.getUUID());
        return "/settings";
    }
}
