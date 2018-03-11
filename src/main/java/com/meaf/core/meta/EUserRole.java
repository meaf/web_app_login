package com.meaf.core.meta;

import com.meaf.core.entities.Role;

public enum EUserRole {
    admin(0L),
    user(1L),
    expert(2L),
    organizer(3L);

    private final Long id;

    EUserRole(Long id) {
        this.id = id;
    }

    public boolean isRole(Role role) {

        return name().equals(role.getRolename());
    }
}
