package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.SettingName;
import com.baikalsr.queueserver.entity.SettingProgram;
import com.baikalsr.queueserver.repository.SettingProgramRepo;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsProgramService {
    @Autowired
    private SettingProgramRepo settingProgramRepo;

    public int getHoursWorkingTime() {
        SettingProgram settingProgram = settingProgramRepo.getBySettingName(SettingName.HOURS_WORKING_TIME);
        return settingProgram.getIntValue();
    }

    public void setHoursWorkingTime(int hoursWorkingTime) {
        SettingProgram settingProgram = new SettingProgram();
        settingProgram.setIntValue(hoursWorkingTime);
        settingProgram.setSettingName(SettingName.HOURS_WORKING_TIME);
        settingProgramRepo.save(settingProgram);
    }
}
