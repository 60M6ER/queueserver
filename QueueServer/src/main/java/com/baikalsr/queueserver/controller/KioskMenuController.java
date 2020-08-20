package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.UISettings;
import com.baikalsr.queueserver.UI.editorImpl.KioskMenuEdit;
import com.baikalsr.queueserver.entity.KioskMenu;
import com.baikalsr.queueserver.repository.KioskMenuRepo;
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

import javax.servlet.http.HttpServletRequest;

@Controller
public class KioskMenuController {
    @Autowired
    private KioskMenuRepo kioskMenuRepo;

    @Autowired
    private KioskMenuEdit kioskMenuEdit;

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

    @RequestMapping("/settings/kioskMenu")
    public String userSet(Model model, @RequestParam Long id){

        KioskMenuEdit newKioskMenuEdit = kioskMenuEdit.cloneObj();

        if (id == -1) //Создаем нового
            newKioskMenuEdit.rebuildKioskMenuEdit(new KioskMenu());
        else //Редактируем существующего
            newKioskMenuEdit.rebuildKioskMenuEdit(kioskMenuRepo.getOne(id));

        UISettings.poolEditObjects.put(newKioskMenuEdit.getUUID(), newKioskMenuEdit);
        model.addAttribute("kioskMenu", newKioskMenuEdit);
        model.addAttribute("errorPass", "");

        return "kioskmenu";
    }

    @RequestMapping(value = "/saveKioskMenu", params = {"addKioskMenu"}, method = RequestMethod.POST)
    public String addRoleUser(@ModelAttribute("kioskMenu") KioskMenuEdit kioskMenuEdit, HttpServletRequest req, Model model){
        KioskMenuEdit kioskMenuEditPool = (KioskMenuEdit) UISettings.poolEditObjects.get(kioskMenuEdit.getUUID());
        if (kioskMenuEdit.getAddingKioskMenu() != null) {
            kioskMenuEditPool.getUnderKioskMenu().add(kioskMenuEdit.getAddingKioskMenu());
        }
        kioskMenuEdit.updateKioskMenuEdit(kioskMenuEditPool);

        model.addAttribute("kioskMenu", kioskMenuEdit);
        model.addAttribute("errorPass", "");
        return "kioskmenu";
    }

    @RequestMapping(value = "/saveKioskMenu", params = {"delKioskMenu"}, method = RequestMethod.POST)
    public String delRoleUser(@ModelAttribute("kioskMenu") KioskMenuEdit kioskMenuEdit, HttpServletRequest req, Model model){
        KioskMenuEdit kioskMenuEditPool = (KioskMenuEdit) UISettings.poolEditObjects.get(kioskMenuEdit.getUUID());
        kioskMenuEditPool.getUnderKioskMenu().remove(Integer.parseInt(req.getParameter("delKioskMenu")));
        kioskMenuEdit.updateKioskMenuEdit(kioskMenuEditPool);
        model.addAttribute("kioskMenu", kioskMenuEdit);
        model.addAttribute("errorPass", "");
        return "kioskmenu";
    }

    @RequestMapping(value = "/saveKioskMenu", params = {"save"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("kioskMenu") KioskMenuEdit kioskMenuEdit, Model model){
        KioskMenuEdit kioskMenuEditPool = (KioskMenuEdit) UISettings.poolEditObjects.get(kioskMenuEdit.getUUID());
        kioskMenuEdit.updateKioskMenuEdit(kioskMenuEditPool);
        model.addAttribute("kioskMenu", kioskMenuEdit);

        KioskMenu kioskMenu = new KioskMenu(kioskMenuEdit);
        kioskMenuRepo.save(kioskMenu);
        UISettings.poolEditObjects.remove(kioskMenuEdit.getUUID());
        return "/settings";
    }
}
