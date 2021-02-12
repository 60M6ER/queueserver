package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.editorImpl.TicketServiceEdit;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class TicketService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private int priority;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private List<Manager> managers;

    private boolean isPause;
    private boolean obligatoryContractor;
    private boolean displayOnTablo;
    private boolean BSService;

    @Enumerated(EnumType.STRING)
    private TypeService TypeStr;
    private String prefix;
    private Status status;


    public TicketService() {
    }

    public TicketService(TicketServiceEdit serviceEdit) {
        id = serviceEdit.getId();
        name = serviceEdit.getName();
        priority = serviceEdit.getPriority();
        managers = serviceEdit.getManagers();
        isPause = serviceEdit.isSupportPause();
        status = serviceEdit.getStatus();
        obligatoryContractor = serviceEdit.isObligatoryContractor();
        displayOnTablo = serviceEdit.isDisplayOnTablo();
        prefix = serviceEdit.getPrefix();
        BSService = serviceEdit.isBSService();
        TypeStr = serviceEdit.getTypeService();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        this.isPause = pause;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isObligatoryContractor() {
        return obligatoryContractor;
    }

    public void setObligatoryContractor(boolean obligatoryContractor) {
        this.obligatoryContractor = obligatoryContractor;
    }

    public boolean isDisplayOnTablo() {
        return displayOnTablo;
    }

    public void setDisplayOnTablo(boolean displayOnTablo) {
        this.displayOnTablo = displayOnTablo;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isBSService() {
        return BSService;
    }

    public void setBSService(boolean BSService) {
        this.BSService = BSService;
    }

    public TypeService getTypeStr() {
        return TypeStr;
    }

    public void setTypeStr(TypeService typeStr) {
        TypeStr = typeStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == String.class)
            return obj.equals("" + id);
        return id == ((TicketService) obj).id;
    }
    @Override
    public String toString() {
        return name;
    }
    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }
}
