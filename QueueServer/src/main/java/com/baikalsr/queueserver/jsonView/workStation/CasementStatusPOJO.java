package com.baikalsr.queueserver.jsonView.workStation;

import java.util.List;
import java.util.Map;

public class CasementStatusPOJO {
    private int casement;
    private String nameManager;
    private Map<String, String> status;
    private List<Map<String, String>> availableStatuses;

    public int getCasement() {
        return casement;
    }

    public void setCasement(int casement) {
        this.casement = casement;
    }

    public String getNameManager() {
        return nameManager;
    }

    public void setNameManager(String nameManager) {
        this.nameManager = nameManager;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public List<Map<String, String>> getAvailableStatuses() {
        return availableStatuses;
    }

    public void setAvailableStatuses(List<Map<String, String>> availableStatuses) {
        this.availableStatuses = availableStatuses;
    }
}
