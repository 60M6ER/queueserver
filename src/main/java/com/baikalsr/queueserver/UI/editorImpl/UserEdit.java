package com.baikalsr.queueserver.UI.editorImpl;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.entity.TicketService;
import com.baikalsr.queueserver.repository.TicketServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class UserEdit {
    @Autowired
    private TicketServiceRepo ticketServiceRepo;

    private String UUID;
    private Long id;
    private String loginAD;
    private String currentPassword;
    private String newPassword;
    private String repitPassword;
    private String name;
    private boolean active;
    private Set<Role> roles;
    private Queue queue;
    private List<TicketService> ticketServices;
    private Role addingRole;
    private TicketService addingService;
    private String newSecurityPass;

    public ArrayList<Role> getSelectRoles() {
        ArrayList<Role> selectRoles = new ArrayList<>();

        ArrayList<Role> allRoles = new ArrayList<Role>();
        Collections.addAll(allRoles, Role.values());

        for (Role roleAll : allRoles) {
            boolean found = false;
            if (this.getRoles()!=null && this.getRoles().size()>0)
                for (Role role : roles) {
                    if (role.equals(roleAll)) {
                        found = true;
                        break;
                    }
                }
            if (!found)
                selectRoles.add(roleAll);
        }
        return selectRoles;
    }

    public ArrayList<TicketService> getSelectServices() {
        ArrayList<TicketService> selectServices = new ArrayList<>();

        List<TicketService> allServices = ticketServiceRepo.findAll();


        for (TicketService serviceAll : allServices) {
            boolean found = false;
            if (this.getTicketServices()!=null && this.getTicketServices().size()>0)
                for (TicketService service : ticketServices) {
                    if (service.equals(serviceAll)) {
                        found = true;
                        break;
                    }
                }
            if (!found)
                selectServices.add(serviceAll);
        }
        return selectServices;
    }

    public UserEdit() {
        UUID = java.util.UUID.randomUUID().toString();
    }


    public UserEdit cloneObj(){
        UserEdit userEdit = new UserEdit();
        userEdit.setTicketServiceRepo(this.ticketServiceRepo);
        return userEdit;
    }

    public UserEdit(Manager manager) {
        UUID = java.util.UUID.randomUUID().toString();
        id = manager.getId();
        loginAD = manager.getLoginAD();
        name = manager.getName();
        active = manager.isActive();
        roles = manager.getRoles();
        queue = manager.getQueue();
        ticketServices = manager.getTicketServices();
    }

    public void setTicketServiceRepo(TicketServiceRepo ticketServiceRepo) {
        this.ticketServiceRepo = ticketServiceRepo;
    }

    public String getNewSecurityPass() {
        return newSecurityPass;
    }

    public void setNewSecurityPass(String newSecurityPass) {
        this.newSecurityPass = newSecurityPass;
    }

    public void rebuildUserEdit(Manager manager) {
        id = manager.getId();
        loginAD = manager.getLoginAD();
        name = manager.getName();
        active = manager.isActive();
        if (manager.getRoles() == null)
            roles = new HashSet<>();
        else
            roles = manager.getRoles();
        queue = manager.getQueue();
        if (manager.getTicketServices()==null)
            ticketServices = new ArrayList<>();
        else
            ticketServices = manager.getTicketServices();
    }

    public void updateUserEdit(UserEdit userEdit) {
        this.ticketServiceRepo = userEdit.getTicketServiceRepo();
        this.setRoles(userEdit.getRoles());
        this.setTicketServices(userEdit.getTicketServices());
    }

    public TicketServiceRepo getTicketServiceRepo() {
        return ticketServiceRepo;
    }

    public TicketService getAddingService() {
        return addingService;
    }

    public void setAddingService(TicketService addingService) {
        this.addingService = addingService;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Role getAddingRole() {
        return addingRole;
    }

    public void setAddingRole(Role addingRole) {
        this.addingRole = addingRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginAD() {
        return loginAD;
    }

    public void setLoginAD(String loginAD) {
        this.loginAD = loginAD;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepitPassword() {
        return repitPassword;
    }

    public void setRepitPassword(String repitPassword) {
        this.repitPassword = repitPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public List<TicketService> getTicketServices() {
        return ticketServices;
    }

    public void setTicketServices(List<TicketService> ticketServices) {
        this.ticketServices = ticketServices;
    }
}
