package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.entity.Kiosk;
import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.repository.KioskRepo;
import com.baikalsr.queueserver.repository.ManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
public class WebController {

    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private KioskRepo kioskRepo;

    @GetMapping("/")
    public String home(Model model){
        String nameUser = "";
        boolean isAdministrator = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (userDetails != null) {
                nameUser = userDetails.getUsername();
                Set<Role> roles = (Set<Role>) userDetails.getAuthorities();
                for (Role role: roles) {
                    isAdministrator = role == Role.ADMINISTRATOR;
                    if (isAdministrator)
                        break;
                }
            }
        }
        System.out.println(nameUser);
        model.addAttribute("nameUser", nameUser);
        model.addAttribute("isAdministrator", isAdministrator);
        return "index";
    }

    @RequestMapping("/settings")
    public String settings(Model model, @RequestParam(required = false) String currentSet){
        if (currentSet != null)
            if (currentSet.equals("Users")) {
                List<Manager> managers = managerRepo.findAll();
                model.addAttribute("Users", managers);
                model.addAttribute("currentSet", currentSet);
            }
            if (currentSet.equals("Kiosks")) {
                List<Kiosk> kiosks = kioskRepo.findAll();
                model.addAttribute("Kiosks", kiosks);
                model.addAttribute("currentSet", currentSet);
            }
        return "settings";
    }
}
