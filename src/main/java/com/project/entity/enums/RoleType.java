package com.project.entity.enums;

public enum RoleType {

    ADMIN("Admin"),
    DOCTOR("Doctor"),
    PATIENT("Patient"),
    MANAGER("Manager"),
    ASSISTANT_MANAGER("AssistenManager");

    public final String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
