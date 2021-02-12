package com.baikalsr.queueserver.jsonView.administration;

import java.util.HashMap;
import java.util.List;

public class QueuesPOJO {
    private long current_queue;
    private List<QueuePOJO> queues;

    public long getCurrent_queue() {
        return current_queue;
    }

    public void setCurrent_queue(long current_queue) {
        this.current_queue = current_queue;
    }

    public List<QueuePOJO> getQueues() {
        return queues;
    }

    public void setQueues(List<QueuePOJO> queues) {
        this.queues = queues;
    }

    public static class QueuePOJO {
        private long value;
        private String name;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
