package com.larionov.converter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartLogic implements CommandLineRunner {

    @Autowired
    private CbrGetService cbrGetService;

    @Override
    public void run(String... args) throws Exception {
        cbrGetService.updateCurrency();
    }
}
