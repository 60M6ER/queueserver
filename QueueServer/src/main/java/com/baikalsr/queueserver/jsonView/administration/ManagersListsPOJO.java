package com.baikalsr.queueserver.jsonView.administration;

import java.util.ArrayList;
import java.util.List;

public class ManagersListsPOJO {
    private List<ManagerPOJO> withQueueList;
    private List<ManagerPOJO> withoutQueueList;

    public ManagersListsPOJO() {
        withoutQueueList = new ArrayList<>();
        withQueueList = new ArrayList<>();
    }

    public void addManagerWithQueue(ManagerPOJO managerPOJO) {
        withQueueList.add(managerPOJO);
    }
    public void addManagerWithoutQueue(ManagerPOJO managerPOJO) {
        withoutQueueList.add(managerPOJO);
    }

    public List<ManagerPOJO> getWithQueueList() {
        return withQueueList;
    }

    public void setWithQueueList(List<ManagerPOJO> withQueueList) {
        this.withQueueList = withQueueList;
    }

    public List<ManagerPOJO> getWithoutQueueList() {
        return withoutQueueList;
    }

    public void setWithoutQueueList(List<ManagerPOJO> withoutQueueList) {
        this.withoutQueueList = withoutQueueList;
    }

    public static class ManagerPOJO{
        private long count;
        private long id;
        private String name;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

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
    }
}
