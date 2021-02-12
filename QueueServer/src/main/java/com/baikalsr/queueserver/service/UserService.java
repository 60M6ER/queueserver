package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.repository.ManagerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthProvider.class);

    @Autowired
    private ManagerRepo managerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager userFindByUsername = managerRepo.findFirstByLoginAD(username);


        if(userFindByUsername != null)
        {
            return userFindByUsername;
        }

        return null;
    }

    public Manager createUserFromAD(String login, String principal) {
        int startI = principal.indexOf("CN=");
        if (startI == -1)
            throw new RuntimeException("Не удалось распарсить строку principal");

        int endI = principal.indexOf(",", startI);
        if (endI == -1)
            throw new RuntimeException("Не удалось распарсить строку principal");

        Manager manager = new Manager();
        manager.setLoginAD(login);
        manager.setName(principal.substring(startI + 3, endI));
        manager.setActive(true);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        manager.setRoles(roles);
        LOGGER.info(String.format("Получен пользователь из AD (%s)", manager.getLoginAD()));
        return managerRepo.save(manager);
    }
}
