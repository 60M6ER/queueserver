package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.UISettings;
import com.baikalsr.queueserver.UI.editorImpl.KioskEdit;
import com.baikalsr.queueserver.entity.Kiosk;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KioskController {
    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private KioskRepo kioskRepo;
    @Autowired
    private KioskEdit kioskEdit;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @RequestMapping("/settings/kiosk")
    public String userSet(Model model, @RequestParam Long id){

        KioskEdit newKioskEdit = kioskEdit.cloneObj();

        if (id == -1) //Создаем нового
            newKioskEdit.rebuildKioskEdit(new Kiosk());
        else //Редактируем существующего
            newKioskEdit.rebuildKioskEdit(kioskRepo.getOne(id));

        UISettings.poolEditObjects.put(newKioskEdit.getUUID(), newKioskEdit);
        model.addAttribute("kiosk", newKioskEdit);
        model.addAttribute("errorPass", "");

        return "kioskedit";
    }

    @RequestMapping(value = "/saveKiosk", params = {"save"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("kiosk") KioskEdit kioskEdit, Model model){
        KioskEdit kioskEditPool = (KioskEdit) UISettings.poolEditObjects.get(kioskEdit.getUUID());
        kioskEdit.updateKioskEdit(kioskEditPool);
        model.addAttribute("kiosk", kioskEdit);

        Kiosk kiosk = new Kiosk(kioskEdit);
        kioskRepo.save(kiosk);
        UISettings.poolEditObjects.remove(kioskEdit.getUUID());
        return "/settings";
    }
}
