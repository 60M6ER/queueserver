package com.baikalsr.queueserver.controller;

import com.baikalsr.queueserver.jsonView.ResponseStatus;
import com.baikalsr.queueserver.jsonView.settings.SettingHoursWorkingTimePOJO;
import com.baikalsr.queueserver.service.SettingsProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/setting_program")
public class SettingProgramRESTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingProgramRESTService.class);

    @Autowired
    private SettingsProgramService settingsProgramService;

    @RequestMapping(value = "/getHoursWorkingTime", method = RequestMethod.GET)
    private int getHoursWorkingTime(HttpServletRequest req) {
        return settingsProgramService.getHoursWorkingTime();
    }

    @PostMapping(value = "/setHoursWorkingTime", consumes = "application/json", produces = "application/json")
    private ResponseStatus setHoursWorkingTime(@RequestBody SettingHoursWorkingTimePOJO hoursWorkingTimePOJO) {

        try {
            if (hoursWorkingTimePOJO.getValue() > 0) {
                settingsProgramService.setHoursWorkingTime(hoursWorkingTimePOJO.getValue());
            }else {
                throw new RuntimeException("Время должно быть больше 0.");
            }
            return new ResponseStatus("ok");
        } catch (Exception e) {
            LOGGER.error("Ошибка при работе с REST. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
