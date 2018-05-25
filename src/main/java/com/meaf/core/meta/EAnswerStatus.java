package com.meaf.core.meta;

public enum EAnswerStatus {
    NEW("Не почато"),
    DRAFT("Чернетка"),
    SUBMITTED("Відправлено"),
    CLOSED("Закрито"),
    DELETED("Видалено");

    String local;

    EAnswerStatus(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
