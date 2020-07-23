package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.TableEditor;
import com.baikalsr.queueserver.UI.UIEditEntities;
import com.baikalsr.queueserver.UI.editorImpl.FieldsObject;
import com.baikalsr.queueserver.UI.editorImpl.TypeField;
import com.baikalsr.queueserver.UI.editorImpl.UserEdit;
import com.baikalsr.queueserver.repository.ManagerRepo;
import com.baikalsr.queueserver.repository.QueueRepo;
import com.sun.istack.Interned;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
public class Manager implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String loginAD;

    @NotNull
    private String password;

    @NotNull
    private String name;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    @ManyToMany(mappedBy = "managers", fetch = FetchType.EAGER)
    private List<TicketService> ticketServices;

    public Manager() {
    }

    public Manager(UserEdit userEdit) {
        this.id = userEdit.getId();
        this.loginAD = userEdit.getLoginAD();
        this.name = userEdit.getName();
        this.active = userEdit.isActive();
        this.roles = userEdit.getRoles();
        this.queue = userEdit.getQueue();
        this.ticketServices = userEdit.getTicketServices();
        if (!(userEdit.getNewSecurityPass()==null))
            this.password = userEdit.getNewSecurityPass();
        else
            this.password = userEdit.getCurrentPassword();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return getRoles();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return isActive() == manager.isActive() &&
                Objects.equals(getId(), manager.getId()) &&
                Objects.equals(getUsername(), manager.getUsername()) &&
                Objects.equals(getPassword(), manager.getPassword()) &&
                Objects.equals(getName(), manager.getName()) &&
                Objects.equals(getRoles(), manager.getRoles());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId(), getUsername(), getPassword(), getName(), isActive(), getRoles());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoginAD(String loginAD) {
        this.loginAD = loginAD;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getLoginAD() {
        return loginAD;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getLoginAD();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Role> getRoles() {
        return roles;
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
