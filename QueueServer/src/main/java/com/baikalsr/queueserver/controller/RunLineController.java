package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.UISettings;
import com.baikalsr.queueserver.UI.editorImpl.RunLineTextEdit;
import com.baikalsr.queueserver.UI.editorImpl.TabloEdit;
import com.baikalsr.queueserver.entity.RunLineText;
import com.baikalsr.queueserver.entity.Tablo;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.repository.RunLineTextRepo;
import com.baikalsr.queueserver.repository.TabloRepo;
import com.baikalsr.queueserver.service.SecurityService;
import com.baikalsr.queueserver.service.StatusManager;
import com.baikalsr.queueserver.service.TabloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class RunLineController {

    @Autowired
    private RunLineTextRepo runLineTextRepo;
    @Autowired
    private RunLineTextEdit runLineTextEdit;

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

    @RequestMapping("/settings/runline")
    public String tabloSet(Model model, @RequestParam String id){

        RunLineTextEdit newRunLineTextEdit = runLineTextEdit.cloneObj();

        if (id.equals("-1")) //Создаем нового
            newRunLineTextEdit.rebuildRunLineTextEdit(new RunLineText());
        else //Редактируем существующего
            newRunLineTextEdit.rebuildRunLineTextEdit(runLineTextRepo.getOne(UUID.fromString(id)));

        UISettings.poolEditObjects.put(newRunLineTextEdit.getUUID(), newRunLineTextEdit);
        model.addAttribute("runLine", newRunLineTextEdit);
        model.addAttribute("errorPass", "");

        return "runlineedit";
    }

    @RequestMapping(value = "/saveRunLine", params = {"save"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("runLine") RunLineTextEdit runLineTextEdit, Model model){
        RunLineTextEdit runLineTextEditPool = (RunLineTextEdit) UISettings.poolEditObjects.get(runLineTextEdit.getUUID());
        runLineTextEdit.updateRunLineTextEdit(runLineTextEditPool);
        model.addAttribute("runLine", runLineTextEdit);

        RunLineText runLineText = new RunLineText(runLineTextEdit);
        runLineTextRepo.save(runLineText);
        UISettings.poolEditObjects.remove(runLineTextEdit.getUUID());
        return "redirect:/settings";
    }
}
