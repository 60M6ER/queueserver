package com.baikalsr.queueserver.entity;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {

    USER, MANAGER, ADMINISTRATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
