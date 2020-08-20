package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.repository.ManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueueEdit {
    @Autowired
    private ManagerRepo managerRepo;

    private String UUID;
    private Long id;
    private String name;
    private int quantityTablo;
    private int quantityInform;
    private int timeInform;
    private KioskMenu kioskMenu;
    private List<Manager> managers;
    private Numerator numerator;
    private Manager addingManager;

    public QueueEdit() {
        UUID = java.util.UUID.randomUUID().toString();
    }

    public QueueEdit(Queue queue) {
        UUID = java.util.UUID.randomUUID().toString();
        id = queue.getId();
        name = queue.getName();
        quantityTablo = queue.getQuantityTablo();
        quantityInform = queue.getQuantityInform();
        timeInform = queue.getTimeInform();
        kioskMenu = queue.getKioskMenu();
        managers = queue.getManagers();
        numerator = queue.getNumerator();
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

    public void rebuildQueueEdit(Queue queue) {
        id = queue.getId();
        name = queue.getName();
        quantityTablo = queue.getQuantityTablo();
        quantityInform = queue.getQuantityInform();
        timeInform = queue.getTimeInform();
        kioskMenu = queue.getKioskMenu();
        if (queue.getManagers()==null)
            managers = new ArrayList<>();
        else
            managers = queue.getManagers();
        numerator = queue.getNumerator();
    }

    public void updateQueueEdit(QueueEdit queueEdit) {
        this.managers = queueEdit.getManagers();
        this.managerRepo = queueEdit.getManagerRepo();
    }

    public QueueEdit cloneObj(){
        QueueEdit queueEdit = new QueueEdit();
        queueEdit.setManagerRepo(this.getManagerRepo());
        return queueEdit;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Manager getAddingManager() {
        return addingManager;
    }

    public void setAddingManager(Manager addingManager) {
        this.addingManager = addingManager;
    }

    public ManagerRepo getManagerRepo() {
        return managerRepo;
    }

    public void setManagerRepo(ManagerRepo managerRepo) {
        this.managerRepo = managerRepo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantityTablo() {
        return quantityTablo;
    }

    public void setQuantityTablo(int quantityTablo) {
        this.quantityTablo = quantityTablo;
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

    public KioskMenu getKioskMenu() {
        return kioskMenu;
    }

    public void setKioskMenu(KioskMenu kioskMenu) {
        this.kioskMenu = kioskMenu;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }

    public Numerator getNumerator() {
        return numerator;
    }

    public void setNumerator(Numerator numerator) {
        this.numerator = numerator;
    }
}
