package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.entity.SettingName;
import com.baikalsr.queueserver.entity.SettingProgram;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.SettingProgramRepo;
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
    private SettingProgramRepo settingProgramRepo;
    @Autowired
    private AuthProvider authProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(StartLogic.class);

    @Override
    public void run(String... args) throws Exception {
        long countUsers = managerRepo.count();
        if (countUsers == 0){

            LOGGER.warn("Нет созданных пользователей пользователей!");

            Manager newAdmin = new Manager();
            newAdmin.setActive(true);
            newAdmin.setName("Administrator");
            newAdmin.setLoginAD("administrator");
            newAdmin.setPassword(authProvider.encodePassword("157456"));
            newAdmin.setRoles(Collections.singleton(Role.ADMINISTRATOR));

            managerRepo.save(newAdmin);
            LOGGER.warn("Создан новый пользователь с ролью Администратор");
        }

        //Настройки программы
        SettingProgram hoursWorkingTime = settingProgramRepo.getBySettingName(SettingName.HOURS_WORKING_TIME);
        if (hoursWorkingTime == null || hoursWorkingTime.getIntValue() == 0) {
            hoursWorkingTime = new SettingProgram();
            hoursWorkingTime.setSettingName(SettingName.HOURS_WORKING_TIME);
            hoursWorkingTime.setIntValue(2);
            settingProgramRepo.save(hoursWorkingTime);
        }
    }
}
