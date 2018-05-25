package com.meaf.core.meta;

public enum ESurveyStatus {
    NEW("Не почато"),
    STARTED("Почато"),
    PARTIALY_ANSWERED("Частково завершено"),
    ANSWERED("Завершено"),
    DELETED("Видалено");

    String local;

    ESurveyStatus(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
