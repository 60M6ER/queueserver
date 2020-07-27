package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.*;
import com.baikalsr.queueserver.repository.KioskMenuRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.baikalsr.queueserver.repository.TicketServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
public class KioskMenuEdit {

    @Autowired
    private TicketServiceRepo ticketServiceRepo;
    @Autowired
    private KioskMenuRepo kioskMenuRepo;

    private String UUID;
    private Long id;
    private String name;
    private List<KioskMenu> underKioskMenu;
    private TypeButton typeButton;
    private TicketService ticketService;
    private KioskMenu addingKioskMenu;

    public ArrayList<TypeButton> getSelectTypeButton() {
        ArrayList<TypeButton> selectTypesButton = new ArrayList<>();

        List<TypeButton> allTypesButton = new ArrayList<TypeButton>();;
        Collections.addAll(allTypesButton, TypeButton.values());

        for (TypeButton typeButton : allTypesButton) {
            selectTypesButton.add(typeButton);
        }
        return selectTypesButton;
    }

    public ArrayList<TicketService> getSelectTicketService() {
        ArrayList<TicketService> selectTicketServices = new ArrayList<>();

        List<TicketService> allTicketServices = ticketServiceRepo.findAll();

        for (TicketService ticketService : allTicketServices) {
            selectTicketServices.add(ticketService);
        }
        return selectTicketServices;
    }

    public ArrayList<KioskMenu> getSelectKioskMenu() {
        ArrayList<KioskMenu> selectKioskMenu = new ArrayList<>();

        List<KioskMenu> allKioskMenu = kioskMenuRepo.findAll();


        for (KioskMenu kioskMenuAll : allKioskMenu) {
            boolean found = false;
            if (this.getUnderKioskMenu()!=null && this.getUnderKioskMenu().size()>0)
                for (KioskMenu kioskMenu : this.getUnderKioskMenu()) {
                    if (kioskMenu.equals(kioskMenuAll)) {
                        found = true;
                        break;
                    }
                }
            if (!found)
                selectKioskMenu.add(kioskMenuAll);
        }
        return selectKioskMenu;
    }

    public KioskMenuEdit() {
        UUID = java.util.UUID.randomUUID().toString();
    }


    public KioskMenuEdit cloneObj(){
        KioskMenuEdit kioskMenuEdit = new KioskMenuEdit();
        kioskMenuEdit.setTicketServiceRepo(this.ticketServiceRepo);
        kioskMenuEdit.setKioskMenuRepo(this.kioskMenuRepo);
        return kioskMenuEdit;
    }

    public KioskMenuEdit(KioskMenu kioskMenu) {
        UUID = java.util.UUID.randomUUID().toString();
        id = kioskMenu.getId();
        name = kioskMenu.getName();
        underKioskMenu = kioskMenu.getUnderKioskMenu();
        typeButton = kioskMenu.getTypeButton();
        ticketService = kioskMenu.getTicketService();
    }

    public void rebuildKioskMenuEdit(KioskMenu kioskMenu) {
        id = kioskMenu.getId();
        name = kioskMenu.getName();
        if (kioskMenu.getUnderKioskMenu() == null)
            underKioskMenu = new ArrayList<>();
        else
            underKioskMenu = kioskMenu.getUnderKioskMenu();
        typeButton = kioskMenu.getTypeButton();
        ticketService = kioskMenu.getTicketService();
    }

    public void updateKioskMenuEdit(KioskMenuEdit kioskMenuEdit) {
        this.setKioskMenuRepo(kioskMenuEdit.getKioskMenuRepo());
        this.setTicketServiceRepo(kioskMenuEdit.getTicketServiceRepo());
        this.setUnderKioskMenu(kioskMenuEdit.getUnderKioskMenu());
    }

    public TicketServiceRepo getTicketServiceRepo() {
        return ticketServiceRepo;
    }

    public void setTicketServiceRepo(TicketServiceRepo ticketServiceRepo) {
        this.ticketServiceRepo = ticketServiceRepo;
    }

    public KioskMenuRepo getKioskMenuRepo() {
        return kioskMenuRepo;
    }

    public void setKioskMenuRepo(KioskMenuRepo kioskMenuRepo) {
        this.kioskMenuRepo = kioskMenuRepo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KioskMenu> getUnderKioskMenu() {
        return underKioskMenu;
    }

    public void setUnderKioskMenu(List<KioskMenu> underKioskMenu) {
        this.underKioskMenu = underKioskMenu;
    }

    public TypeButton getTypeButton() {
        return typeButton;
    }

    public void setTypeButton(TypeButton typeButton) {
        this.typeButton = typeButton;
    }

    public KioskMenu getAddingKioskMenu() {
        return addingKioskMenu;
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public TicketService getTicketService() {
        return ticketService;
    }

    public void setAddingKioskMenu(KioskMenu addingKioskMenu) {
        this.addingKioskMenu = addingKioskMenu;
    }
}
