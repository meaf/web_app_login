package com.meaf.core.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYS_PROPERTIES")
public class SysProperties implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return name;
    }

    public void setKey(String name) {
        name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SysProperties(String key, String value) {
        name = key;
        this.value = value;
    }

    public SysProperties() {
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SysProperties && ((SysProperties) obj).getId().equals(id);
    }
}
