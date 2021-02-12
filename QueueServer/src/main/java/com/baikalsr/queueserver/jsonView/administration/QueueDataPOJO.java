package com.baikalsr.queueserver.jsonView.administration;

import java.util.ArrayList;
import java.util.List;

public class QueueDataPOJO {
    private long queueId;
    private boolean active;
    private int quantityInform;
    private int timeInform;
    private List<KioskPOJO> kiosks;
    private List<TabloPOJO> tablos;

    public QueueDataPOJO() {
        kiosks = new ArrayList<>();
        tablos = new ArrayList<>();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getQuantityInform() {
        return quantityInform;
    }

    public void setQuantityInform(int quantityInform) {
        this.quantityInform = quantityInform;
    }

    public int getTimeInform() {
        return timeInform;
    }

    public void setTimeInform(int timeInform) {
        this.timeInform = timeInform;
    }

    public List<KioskPOJO> getKiosks() {
        return kiosks;
    }

    public void setKiosks(List<KioskPOJO> kiosks) {
        this.kiosks = kiosks;
    }

    public List<TabloPOJO> getTablos() {
        return tablos;
    }

    public void setTablos(List<TabloPOJO> tablos) {
        this.tablos = tablos;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }

    public static class KioskPOJO{
        private long id;
        private String name;
        private String comment;
        private boolean active;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class TabloPOJO{
        private long id;
        private String name;
        private String comment;
        private boolean active;
        private int countLinesOnPage;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public int getCountLinesOnPage() {
            return countLinesOnPage;
        }

        public void setCountLinesOnPage(int countLinesOnPage) {
            this.countLinesOnPage = countLinesOnPage;
        }
    }
}
