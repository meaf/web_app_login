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
    private String key;

    @Column(name = "VALUE")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String name) {
        key = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SysProperties(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SysProperties() {
    }
}
