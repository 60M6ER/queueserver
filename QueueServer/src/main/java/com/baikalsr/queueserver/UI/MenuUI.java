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
        rolesAll.add(Role.MANAGER);
        rolesAll.add(Role.ADMINISTRATOR);
        HashSet<Role> rolesAdmin = new HashSet<>();
        rolesAdmin.add(Role.ADMINISTRATOR);
        HashSet<Role> rolesManager = new HashSet<>();
        rolesManager.add(Role.MANAGER);

        setMenuUnit();
        menuUnit.put("name", "Главная");
        menuUnit.put("URL", "/");
        menuUnit.put("Roles", rolesAll);
        menuStruct.add(getMenuUnit());

        setMenuUnit();
        menuUnit.put("name", "Администрирование");
        menuUnit.put("URL", "/administration");
        menuUnit.put("Roles", rolesManager);
        menuStruct.add(getMenuUnit());

        setMenuUnit();
        menuUnit.put("name", "Отчетность");
        menuUnit.put("URL", "/reports");
        menuUnit.put("Roles", rolesManager);
        menuStruct.add(getMenuUnit());

        setMenuUnit();
        menuUnit.put("name", "Рабочее место оператора");
        menuUnit.put("URL", "/workStation");
        menuUnit.put("Roles", rolesAdmin);
        menuStruct.add(getMenuUnit());

        setMenuUnit();
        menuUnit.put("name", "Настройки");
        menuUnit.put("URL", "/settings");
        menuUnit.put("Roles", rolesAdmin);
        menuStruct.add(getMenuUnit());
    }

    public boolean getByRole(Integer index) {
        HashSet<Role> rolesFind = (HashSet<Role>) menuStruct.get(index).get("Roles");

        return securityService.testByRolesUser(rolesFind, null, true);
    }

    public boolean getByRole(HashMap<String, Object> menuUnit) {
        HashSet<Role> rolesFind = (HashSet<Role>) menuUnit.get("Roles");

        return securityService.testByRolesUser(rolesFind, null, true);
    }

    public ArrayList<HashMap<String, Object>> getMenuStruct() {
        return menuStruct;
    }

    public ArrayList<HashMap<String, Object>> getMenuStructByRoles() {
        ArrayList<HashMap<String, Object>> struct = new ArrayList<>();
        for (int i = 0; i < menuStruct.size(); i++) {
            if (securityService.testByRolesUser((HashSet<Role>) menuStruct.get(i).get("Roles"), null, true))
                struct.add(menuStruct.get(i));
        }
        return struct;
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
