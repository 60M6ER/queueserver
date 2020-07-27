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

@Component
public class StartLogic implements CommandLineRunner {

    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(StartLogic.class);

    @Override
    public void run(String... args) throws Exception {
        long countUsers = managerRepo.count();
        if (countUsers == 0){

            LOGGER.warn("Нет созданных пользователей пользователей!");

            Manager newAdmin = new Manager();
            newAdmin.setActive(true);
            newAdmin.setName("Administrator");
            newAdmin.setLoginAD("Administrator");
            newAdmin.setPassword(passwordEncoder.encode("157456"));
            newAdmin.setRoles(Collections.singleton(Role.ADMINISTRATOR));

            managerRepo.save(newAdmin);
            LOGGER.warn("Создан новый пользователь с ролью Администратор");
        }
    }
}
