package com.baikalsr.queueserver.entity;

import com.baikalsr.queueserver.UI.TableEditor;
import com.baikalsr.queueserver.UI.UIEditEntities;
import com.baikalsr.queueserver.UI.editorImpl.FieldsObject;
import com.baikalsr.queueserver.UI.editorImpl.TypeField;
import com.sun.istack.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
public class Manager implements UserDetails, UIEditEntities {
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

    @Override
    public ArrayList<HashMap<String, Object>> getFields() {
        ArrayList<HashMap<String, Object>> fields = new ArrayList<>();

        HashMap<String, Object> structField = new HashMap<>();

        structField.put("name", "Логин (Active Directory):");
        structField.put("type", 0);
        fields.add(structField);

        structField = new HashMap<>();
        structField.put("name", "Ф. И. О.:");
        structField.put("type", 0);
        fields.add(structField);

        structField = new HashMap<>();
        structField.put("name", "Активный:");
        structField.put("type", 1);
        fields.add(structField);

        structField = new HashMap<>();
        structField.put("name", "Очередь:");
        structField.put("type", 2);
        fields.add(structField);

        return fields;
    }


    @Override
    public Object getField(int i) {
        switch (i){
            case 0:
                return this.getLoginAD();
            case 1:
                return this.getName();
            case 2:
                return this.isActive() ? true : false;
        }

        return null;
    }

    @Override
    public TableEditor getTable(int i) {
        return null;
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
