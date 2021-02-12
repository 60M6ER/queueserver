package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.UI.MenuUI;
import com.baikalsr.queueserver.UI.UISettings;
import com.baikalsr.queueserver.UI.editorImpl.UserEdit;
import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.repository.TicketServiceRepo;
import com.baikalsr.queueserver.service.AuthProvider;
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
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ManagerController {
    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private QueueRepo queueRepo;
    @Autowired
    private TicketServiceRepo ticketServiceRepo;
    @Autowired
    private UserEdit userEdit;

    @Autowired
    private AuthProvider authProvider;


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

    @RequestMapping("/settings/user")
    public String userSet(Model model, @RequestParam Long id){

        UserEdit newUserEdit = userEdit.cloneObj();

        if (id == -1) //Создаем нового
            newUserEdit.rebuildUserEdit(new Manager());
        else //Редактируем существующего
            newUserEdit.rebuildUserEdit(managerRepo.getOne(id));

        UISettings.poolEditObjects.put(newUserEdit.getUUID(), newUserEdit);
        model.addAttribute("manager", newUserEdit);
        model.addAttribute("queues", queueRepo.findAll());
        model.addAttribute("errorPass", "");

        return "user";
    }

    @RequestMapping(value = "/saveUser", params = {"addRole"}, method = RequestMethod.POST)
    public String addRoleUser(@ModelAttribute("manger") UserEdit userEdit, HttpServletRequest req, Model model){
        UserEdit userEditPool = (UserEdit) UISettings.poolEditObjects.get(userEdit.getUUID());
        if (userEdit.getAddingRole() != null) {
            userEditPool.getRoles().add(userEdit.getAddingRole());
        }
        userEdit.updateUserEdit(userEditPool);

        model.addAttribute("manager", userEdit);
        model.addAttribute("queues", queueRepo.findAll());
        model.addAttribute("errorPass", "");
        return "user";
    }

    @RequestMapping(value = "/saveUser", params = {"delRole"}, method = RequestMethod.POST)
    public String delRoleUser(@ModelAttribute("manger") UserEdit userEdit, HttpServletRequest req, Model model){
        UserEdit userEditPool = (UserEdit) UISettings.poolEditObjects.get(userEdit.getUUID());
        userEditPool.getRoles().remove(Role.valueOf(req.getParameter("delRole")));
        userEdit.updateUserEdit(userEditPool);
        model.addAttribute("manager", userEdit);
        model.addAttribute("queues", queueRepo.findAll());
        model.addAttribute("errorPass", "");
        return "user";
    }

    @RequestMapping(value = "/saveUser", params = {"addService"}, method = RequestMethod.POST)
    public String addServiceUser(@ModelAttribute("manger") UserEdit userEdit, HttpServletRequest req, Model model){
        UserEdit userEditPool = (UserEdit) UISettings.poolEditObjects.get(userEdit.getUUID());
        if (userEdit.getAddingService() != null) {
            userEditPool.getTicketServices().add(userEdit.getAddingService());
        }
        userEdit.updateUserEdit(userEditPool);

        model.addAttribute("manager", userEdit);
        model.addAttribute("queues", queueRepo.findAll());
        model.addAttribute("errorPass", "");
        return "user";
    }

    @RequestMapping(value = "/saveUser", params = {"delService"}, method = RequestMethod.POST)
    public String delServiceUser(@ModelAttribute("manger") UserEdit userEdit, HttpServletRequest req, Model model){
        UserEdit userEditPool = (UserEdit) UISettings.poolEditObjects.get(userEdit.getUUID());
        userEditPool.getTicketServices().remove(Integer.parseInt(req.getParameter("delService")));
        userEdit.updateUserEdit(userEditPool);
        model.addAttribute("manager", userEdit);
        model.addAttribute("queues", queueRepo.findAll());
        model.addAttribute("errorPass", "");
        return "user";
    }

    @RequestMapping(value = "/saveUser", params = {"save"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("manger") UserEdit userEdit, Model model){
        UserEdit userEditPool = (UserEdit) UISettings.poolEditObjects.get(userEdit.getUUID());
        userEdit.updateUserEdit(userEditPool);
        model.addAttribute("manager", userEdit);
        model.addAttribute("queues", queueRepo.findAll());
        Manager managerDB;
        if (userEdit.getId()==null)
            managerDB = new Manager();
        else
            managerDB = managerRepo.getOne(userEdit.getId());
        if (!userEdit.getNewPassword().equals("")) {
            if (authProvider.matches(userEdit.getCurrentPassword(), managerDB.getPassword()) || managerDB.getPassword()==null) {
                if (!userEdit.getNewPassword().equals(userEdit.getRepitPassword())) {
                    model.addAttribute("errorPass", "Пароли не совпадают");
                    return "user";
                }else {
                    userEdit.setNewSecurityPass(authProvider.encodePassword(userEdit.getNewPassword()));
                }
            } else {
                model.addAttribute("errorPass", "Введен не правильный пароль.");
                return "user";
            }
        }
        userEdit.setCurrentPassword(managerDB.getPassword());
        Manager manager = new Manager(userEdit);
        managerRepo.save(manager);
        UISettings.poolEditObjects.remove(userEdit.getUUID());
        return "redirect:/settings";
    }
}
