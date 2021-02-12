package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.repository.ManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityService {
    @Autowired
    private ManagerRepo managerRepo;


    public boolean testByRolesUser(Collection<Role> rolesFind, Manager manager, boolean trueAdmin) {
        boolean isAdministrator = false;
        boolean result = false;

        Set<Role> roles = null;

        if (manager == null) {
            UserDetails userDetails = getCurrentUserDetails();
            if (userDetails != null)
                roles = (Set<Role>) userDetails.getAuthorities();
        } else {
            roles = manager.getRoles();
        }

        if (roles == null)
            return false;

        for (Role role : roles) {
            isAdministrator = role == Role.ADMINISTRATOR;
            if (isAdministrator) break;
            for (Role roleFind : rolesFind) {
                result = role == roleFind;
                if (result) break;
            }
            if (result) break;
        }

        return (trueAdmin && (isAdministrator || result))
                || result;
    }

    public boolean testByRoleUser(Role role, Manager manager, boolean trueAdmin) {
        HashSet<Role> rolesFind = new HashSet<>();
        rolesFind.add(role);
        return testByRolesUser(rolesFind, manager, trueAdmin);
    }

    public boolean isAdministrator(Manager manager) {
        HashSet<Role> rolesFind = new HashSet<>();
        rolesFind.add(Role.ADMINISTRATOR);
        return testByRolesUser(rolesFind, manager, true);
    }

    public boolean isServiceUser(Manager manager) {
        Set<Role> roles = manager.getRoles();
        return roles.contains(Role.SERVICE);
    }

    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            return  (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    public String getUsername() {
        String nameUser = "";
        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails != null)
            nameUser = userDetails.getUsername();

        return nameUser;
    }

    public Manager getCurrentManager() {
        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails != null)
            return managerRepo.findFirstByLoginAD(userDetails.getUsername());
        return null;
    }

    public String getNameUser() {

        String login = getUsername();

        if (login != "") {
            return managerRepo.findFirstByLoginAD(login).getName();
        }

        return "";
    }
}
