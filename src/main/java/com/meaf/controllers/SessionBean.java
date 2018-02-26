package com.meaf.controllers;

import com.meaf.core.dao.service.UserService;
import com.meaf.core.entities.Role;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {

    private String login;
    private String password;
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void createUser() throws Exception {
        new UserService().addUser(login, password, role);
    }

//    public List<Role> getRolesList(){
//        return userService.getRolesList();
//    }

}