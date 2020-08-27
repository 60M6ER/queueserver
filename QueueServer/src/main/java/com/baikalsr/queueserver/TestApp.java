package com.baikalsr.queueserver;

import com.baikalsr.queueserver.service.Printer;

public class TestApp {
    public static void main(String[] args) {
        Printer printer = new Printer("http://msk-dev-pc13:2525");
        printer.updateStatuses();
    }
}
