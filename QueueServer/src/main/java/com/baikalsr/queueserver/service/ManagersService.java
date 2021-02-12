package com.baikalsr.queueserver.service;

import com.baikalsr.queueserver.entity.ManagersStatus;
import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.repository.ManagersStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ManagersService {

    @Autowired
    private ManagersStatusRepo managersStatusRepo;
    @Autowired
    private SettingsProgramService settingsProgramService;

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public synchronized void controlWorkStatus() {
        Date date = new Date();
        Date oldDate = new Date();
        oldDate.setTime(date.getTime() - (60*60*settingsProgramService.getHoursWorkingTime()*1000));
        List<ManagersStatus> managersStatuses = managersStatusRepo.getAllByOlderDateAndStatus(oldDate, Status.WORKING_TIME.name());
        if (managersStatuses != null && managersStatuses.size() > 0) {
            for (int i = 0; i < managersStatuses.size(); i++) {
                managersStatuses.set(i, managersStatuses.get(i).clone());
                managersStatuses.get(i).setDate(date);
                managersStatuses.get(i).setStatus(Status.NOT_WORKING_TIME);
            }
            managersStatusRepo.saveAll(managersStatuses);
        }
    }
}
