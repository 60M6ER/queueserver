package com.baikalsr.printservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartLogic implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartLogic.class);

    @Autowired
    ServicePrint servicePrint;

    @Override
    public void run(String... args) throws Exception {
        //servicePrint.getStatuses();
        //LOGGER.info("Прошли статовую обработку");
    }
}
