package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.TicketService;
import com.baikalsr.queueserver.repository.ManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketServiceEdit {
    @Autowired
    private ManagerRepo managerRepo;

    private String UUID;
    private Long id;
    private String name;
    private int priority;
    private List<Manager> managers;

    private Manager addingManager;

    public TicketServiceEdit() {
        UUID = java.util.UUID.randomUUID().toString();
    }

    public TicketServiceEdit(TicketService ticketService) {
        UUID = java.util.UUID.randomUUID().toString();
        id = ticketService.getId();
        priority = ticketService.getPriority();
        managers = ticketService.getManagers();
    }

    public ArrayList<Manager> getSelectManagers() {
        ArrayList<Manager> selectManagers = new ArrayList<>();

        List<Manager> allManagers = managerRepo.findAll();


        for (Manager managerAll : allManagers) {
            boolean found = false;
            if (this.getManagers()!=null && this.getManagers().size()>0)
                for (Manager manager : managers) {
                    if (manager.equals(managerAll)) {
                        found = true;
                        break;
                    }
                }
            if (!found)
                selectManagers.add(managerAll);
        }
        return selectManagers;
    }

    public void rebuildServiceEdit(TicketService ticketService) {
        id = ticketService.getId();
        name = ticketService.getName();
        priority = ticketService.getPriority();
        if (ticketService.getManagers()==null)
            managers = new ArrayList<>();
        else
            managers = ticketService.getManagers();
    }

    public void updateServiceEdit(TicketServiceEdit serviceEdit) {
        this.managers = serviceEdit.getManagers();
        this.managerRepo = serviceEdit.getManagerRepo();
    }

    public TicketServiceEdit cloneObj(){
        TicketServiceEdit serviceEdit = new TicketServiceEdit();
        serviceEdit.setManagerRepo(this.getManagerRepo());
        return serviceEdit;
    }

    public String getUUID() {
        return UUID;
    }

    public Manager getAddingManager() {
        return addingManager;
    }

    public void setAddingManager(Manager addingManager) {
        this.addingManager = addingManager;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManagerRepo getManagerRepo() {
        return managerRepo;
    }

    public void setManagerRepo(ManagerRepo managerRepo) {
        this.managerRepo = managerRepo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }
}
