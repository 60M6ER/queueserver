package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.UISettings;
import com.baikalsr.queueserver.UI.editorImpl.KioskEdit;
import com.baikalsr.queueserver.UI.editorImpl.TabloEdit;
import com.baikalsr.queueserver.entity.Kiosk;
import com.baikalsr.queueserver.entity.Tablo;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.repository.TabloRepo;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import com.baikalsr.queueserver.service.TabloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class TabloController {
    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private TabloRepo tabloRepo;
    @Autowired
    private TabloEdit tabloEdit;

    @Autowired
    private TabloService tabloService;
    @Autowired
    private WebController webController;
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
    public ArrayList<HashMap<String, Object>> getMenuUI() {
        return menuUI.getMenuStructByRoles();
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

    @RequestMapping("/settings/tablo")
    public String tabloSet(Model model, @RequestParam Long id){

        TabloEdit newTabloEdit = tabloEdit.cloneObj();

        if (id == -1) //Создаем нового
            newTabloEdit.rebuildTabloEdit(new Tablo());
        else //Редактируем существующего
            newTabloEdit.rebuildTabloEdit(tabloRepo.getOne(id));

        UISettings.poolEditObjects.put(newTabloEdit.getUUID(), newTabloEdit);
        model.addAttribute("tablo", newTabloEdit);
        model.addAttribute("errorPass", "");

        return "tabloedit";
    }

    @RequestMapping(value = "/saveTablo", params = {"save"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("tablo") TabloEdit tabloEdit, Model model){
        TabloEdit tabloEditPool = (TabloEdit) UISettings.poolEditObjects.get(tabloEdit.getUUID());
        tabloEdit.updateTabloEdit(tabloEditPool);
        model.addAttribute("tablo", tabloEdit);

        Tablo tablo = new Tablo(tabloEdit);
        tabloRepo.save(tablo);
        UISettings.poolEditObjects.remove(tabloEdit.getUUID());
        return "redirect:/settings";
    }


    @RequestMapping(value = "/settings/tabloUpdateAll", method = RequestMethod.GET)
    public String tabloUpdateAll(Model model){

        tabloService.allUpdate();

        return webController.settings(model, "Tablos");
    }
}
