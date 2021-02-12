package com.baikalsr.queueserver.jsonView.tablo;

import java.util.List;
import java.util.UUID;

public class TicketsListForTabloPOJO {

    private List<TalonToCasement> talonsToCasement;
    private List<TalonInQueue> talonsInQueue;
    private boolean update;
    private boolean active;

    public List<TalonToCasement> getTalonsToCasement() {
        return talonsToCasement;
    }

    public void setTalonsToCasement(List<TalonToCasement> talonsToCasement) {
        this.talonsToCasement = talonsToCasement;
    }

    public List<TalonInQueue> getTalonsInQueue() {
        return talonsInQueue;
    }

    public void setTalonsInQueue(List<TalonInQueue> talonsInQueue) {
        this.talonsInQueue = talonsInQueue;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static class TalonToCasement{
        private String id;
        private String name;
        private int casement;

        public TalonToCasement() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCasement() {
            return casement;
        }

        public void setCasement(int casement) {
            this.casement = casement;
        }
    }

    public static class TalonInQueue{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
