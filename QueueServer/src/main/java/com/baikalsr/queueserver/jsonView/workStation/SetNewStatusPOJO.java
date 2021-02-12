package com.baikalsr.queueserver.jsonView.workStation;

import com.baikalsr.queueserver.entity.Status;

public class SetNewStatusPOJO {
    private String idUser;
    private Status valueNewStatus;
    private int casement;

    public SetNewStatusPOJO() {
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Status getValueNewStatus() {
        return valueNewStatus;
    }

    public void setValueNewStatus(Status status) {
        this.valueNewStatus = status;
    }

    public void setValueNewStatus(String status) {
        this.valueNewStatus = Status.valueOf(status);
    }

    public int getCasement() {
        return casement;
    }

    public void setCasement(int casement) {
        this.casement = casement;
    }
}
