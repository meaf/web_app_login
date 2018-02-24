package com.meaf.web;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Date;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by meaf on 16.02.18.
 */
@SessionScoped
@ManagedBean
public class HelloBean {


    @PostConstruct
    public void infoC() {
        date = new Date();
    }

    @PreDestroy
    public void infoD() {
    }

    private String name;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
