package com.baikalsr.queueserver.UI;

import com.baikalsr.queueserver.entity.Role;
import com.baikalsr.queueserver.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class MenuUI {

    @Autowired
    private SecurityService securityService;

    private ArrayList<HashMap<String, Object>> menuStruct;
    private HashMap<String, Object> menuUnit;

    public MenuUI() {
        menuStruct = new ArrayList<>();

        HashSet<Role> rolesAll = new HashSet<>();
        rolesAll.add(Role.USER);
        HashSet<Role> rolesAdmin = new HashSet<>();
        rolesAll.add(Role.ADMINISTRATOR);

        setMenuUnit();
        menuUnit.put("name", "Главная");
        menuUnit.put("URL", "/");
        menuUnit.put("Roles", rolesAll);
        menuStruct.add(getMenuUnit());

        setMenuUnit();
        menuUnit.put("name", "Рабочее место оператора");
        menuUnit.put("URL", "/workStation");
        menuUnit.put("Roles", rolesAll);
        menuStruct.add(getMenuUnit());

        setMenuUnit();
        menuUnit.put("name", "Настройки");
        menuUnit.put("URL", "/settings");
        menuUnit.put("Roles", rolesAdmin);
        menuStruct.add(getMenuUnit());
    }

    public boolean getByRole(Integer index) {
        HashSet<Role> rolesFind = (HashSet<Role>) menuStruct.get(index).get("Roles");

        return securityService.testByRolesUser(rolesFind);
    }

    public boolean getByRole(HashMap<String, Object> menuUnit) {
        HashSet<Role> rolesFind = (HashSet<Role>) menuUnit.get("Roles");

        return securityService.testByRolesUser(rolesFind);
    }

    public ArrayList<HashMap<String, Object>> getMenuStruct() {
        return menuStruct;
    }

    public void setMenuStruct(ArrayList<HashMap<String, Object>> menuStruct) {
        this.menuStruct = menuStruct;
    }

    public HashMap<String, Object> getMenuUnit() {
        return menuUnit;
    }

    public void setMenuUnit() {
        this.menuUnit = new HashMap<>();
    }
}
