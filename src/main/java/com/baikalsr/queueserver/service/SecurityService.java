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


    public boolean testByRolesUser(Collection<Role> rolesFind) {
        boolean isAdministrator = false;
        boolean result = false;

        UserDetails userDetails = getCurrentUserDetails();
        if (userDetails != null) {
            Set<Role> roles = (Set<Role>) userDetails.getAuthorities();
            for (Role role : roles) {
                isAdministrator = role == Role.ADMINISTRATOR;
                if (isAdministrator) break;
                for (Role roleFind : rolesFind) {
                    result = role == roleFind;
                    if (result) break;
                }
            }
        }

        return isAdministrator || result;
    }

    public boolean testByRoleUser(Role role) {
        HashSet<Role> rolesFind = new HashSet<>();
        rolesFind.add(role);
        return testByRolesUser(rolesFind);
    }

    public boolean isAdministrator() {
        HashSet<Role> rolesFind = new HashSet<>();
        rolesFind.add(Role.ADMINISTRATOR);
        return testByRolesUser(rolesFind);
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
            return managerRepo.findByLoginAD(userDetails.getUsername());
        return null;
    }

    public String getNameUser() {

        String login = getUsername();

        if (login != "") {
            return managerRepo.findByLoginAD(login).getName();
        }

        return "";
    }
}
