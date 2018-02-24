package com.meaf.web;

import com.meaf.entities.User;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ManagedBean
@SessionScoped
public class ExampleCDI implements Serializable{

    private String login;
    private String password;

    private boolean loginSuccess;
    private boolean createSuccess;

    @EJB
    private ExampleEJB exampleEJB;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public boolean isCreateSuccess() {
        return createSuccess;
    }

    public void setCreateSuccess(boolean createSuccess) {
        this.createSuccess = createSuccess;
    }

    public List<User> getAllUsers(){
        return exampleEJB.getAllUsers();
    }

    public void createUser() throws Exception {
        exampleEJB.addAdmin(login, password);
    }
}