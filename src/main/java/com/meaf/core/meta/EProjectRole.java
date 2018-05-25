package com.meaf.core.meta;

public enum EProjectRole {
    EXPERT("Експерт"),
    ORGANIZER("Організатор"),
    ADMIN("Адміністратор");

    String local;

    EProjectRole(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
