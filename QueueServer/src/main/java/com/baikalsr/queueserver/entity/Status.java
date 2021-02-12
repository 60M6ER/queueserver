package com.baikalsr.queueserver.entity;


public enum Status {
    INDIVIDUAL_TIME, NOT_WORKING_TIME, WAIT_CLIENT, SERVICING_CLIENT, SERVICING_REGULAR_CLIENT, RECEPTION_EXPEDITION, WORKING_TIME;

    public static String getRussianName(Status status) {
        if (status == INDIVIDUAL_TIME)
            return "Личное время";
        if (status == NOT_WORKING_TIME)
            return "Нерабочее время";
        if (status == WAIT_CLIENT)
            return "Ожидание клиента";
        if (status == SERVICING_CLIENT)
            return "Обслуживание клиента";
        if (status == SERVICING_REGULAR_CLIENT)
            return "Обслуживание постоянного клиента";
        if (status == RECEPTION_EXPEDITION)
            return "Обработка экспедиции";
        if (status == WORKING_TIME)
            return "Рабочее время";
        return "Статус непоределен!";
    }

    public static Status[] getAvailableStatuses(Status status) {
        if (status == INDIVIDUAL_TIME || status == NOT_WORKING_TIME ||
                status == RECEPTION_EXPEDITION || status == WORKING_TIME) {
            Status[] res = new Status[5];
            res[0] = NOT_WORKING_TIME;
            res[1] = SERVICING_REGULAR_CLIENT;
            res[2] = RECEPTION_EXPEDITION;
            res[3] = WORKING_TIME;
            res[4] = INDIVIDUAL_TIME;
            return res;
        }
        if (status == WAIT_CLIENT) {
            Status[] res = new Status[1];
            res[0] = WAIT_CLIENT;
            return res;
        }
        if (status == SERVICING_CLIENT) {
            Status[] res = new Status[3];
            res[0] = NOT_WORKING_TIME;
            res[1] = INDIVIDUAL_TIME;
            res[2] = SERVICING_CLIENT;
            return res;
        }
        if (status == SERVICING_REGULAR_CLIENT) {
            Status[] res = new Status[3];
            res[0] = NOT_WORKING_TIME;
            res[1] = INDIVIDUAL_TIME;
            res[2] = SERVICING_REGULAR_CLIENT;
            return res;
        }
        Status[] res = new Status[1];
        res[0] = NOT_WORKING_TIME;
        return res;
    }

    public static Status[] getAllStatusForChange(){
        Status[] res = new Status[5];
        res[0] = NOT_WORKING_TIME;
        res[1] = SERVICING_REGULAR_CLIENT;
        res[2] = RECEPTION_EXPEDITION;
        res[3] = WORKING_TIME;
        res[4] = INDIVIDUAL_TIME;
        return res;
    }
}
