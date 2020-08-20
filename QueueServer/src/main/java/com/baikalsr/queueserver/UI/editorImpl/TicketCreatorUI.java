package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.TicketService;


public class TicketCreatorUI {
    private Queue queue;
    private TicketService service;

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public TicketService getService() {
        return service;
    }

    public void setService(TicketService service) {
        this.service = service;
    }
}
