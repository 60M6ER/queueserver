package com.baikalsr.queueserver.entity;

public enum  TicketStatus {
    QUEUE, WAIT, SERVICING, PAUSE, ENDED, PRINTING, ERROR_PRINT;

    public static String getRusName(TicketStatus ticketStatus) {
        if (ticketStatus == QUEUE) {
            return "В очереди";
        }
        if (ticketStatus == WAIT) {
            return "В ожидании приема";
        }
        if (ticketStatus == SERVICING) {
            return "Обслуживается";
        }
        if (ticketStatus == PAUSE) {
            return "На паузе";
        }
        if (ticketStatus == ENDED) {
            return "Завершено обслуживание";
        }
        if (ticketStatus == PRINTING) {
            return "В очереди на печать";
        }
        if (ticketStatus == ERROR_PRINT) {
            return "Ошибка печати";
        }
        return "";
    }
}
