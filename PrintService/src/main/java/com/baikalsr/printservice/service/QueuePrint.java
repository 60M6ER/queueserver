package com.baikalsr.printservice.service;

import com.baikalsr.printservice.entity.PrintJob;
import com.baikalsr.printservice.entity.StatusJob;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QueuePrint {
    private Map<UUID, PrintJob> queueJobs;

    public QueuePrint() {
        queueJobs = new LinkedHashMap<>();
    }

    public synchronized PrintJob addJob(PrintJob printJob) {
        PrintJob newPrintJob = new PrintJob(printJob);
        queueJobs.put(newPrintJob.getNumber(), newPrintJob);
        return newPrintJob;
    }

    public synchronized PrintJob getJobByNumber(UUID number) {
        return queueJobs.get(number);
    }

    public synchronized PrintJob getJobForPrint() {
        for (Map.Entry<UUID, PrintJob> printJob : queueJobs.entrySet()) {
            if (printJob.getValue().getStatusJob() == StatusJob.WAIT)
                return printJob.getValue();
        }
        return null;
    }

    public synchronized List<PrintJob> getPrintJobs(){
        List<PrintJob> printJobs = new ArrayList<>();
        for (Map.Entry<UUID, PrintJob> printJob : queueJobs.entrySet()) printJobs.add(printJob.getValue());
        return printJobs;
    }

    public synchronized Map<UUID, PrintJob> getQueueJobs() {
        return queueJobs;
    }

    public synchronized void setQueueJobs(Map<UUID, PrintJob> queueJobs) {
        this.queueJobs = queueJobs;
    }


}
