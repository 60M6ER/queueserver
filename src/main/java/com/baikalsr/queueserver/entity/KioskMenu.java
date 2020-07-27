package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.editorImpl.KioskMenuEdit;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class KioskMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @ElementCollection
    @JoinTable(name = "under_kioskMenu", joinColumns = @JoinColumn(name = "parentKioskMenu_id"))
    private List<KioskMenu> underKioskMenu;

    @Enumerated(EnumType.STRING)
    private TypeButton typeButton;

    @OneToOne
    @JoinColumn(name = "ticket_service_id")
    private TicketService ticketService;

    public KioskMenu() {
    }

    public KioskMenu(KioskMenuEdit kioskMenuEdit) {
        id = kioskMenuEdit.getId();
        name = kioskMenuEdit.getName();
        underKioskMenu = kioskMenuEdit.getUnderKioskMenu();
        typeButton = kioskMenuEdit.getTypeButton();
        ticketService = getTicketService();
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

    public TicketService getTicketService() {
        return ticketService;
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
