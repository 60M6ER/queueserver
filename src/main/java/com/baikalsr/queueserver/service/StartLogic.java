package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.repository.ManagerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class StartLogic implements CommandLineRunner {

    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger("Starting application");

    @Override
    public void run(String... args) throws Exception {
        long countUsers = managerRepo.count();
        if (countUsers == 0){

            logger.warn("Нет пользователей!");

            Manager newAdmin = new Manager();
            newAdmin.setActive(true);
            newAdmin.setName("Administrator");
            newAdmin.setLoginAD("Administrator");
            newAdmin.setPassword(passwordEncoder.encode("157456"));
            newAdmin.setRoles(Collections.singleton(Role.ADMINISTRATOR));

            managerRepo.save(newAdmin);
            logger.warn("Создан новый администратор");
        }
    }
}
